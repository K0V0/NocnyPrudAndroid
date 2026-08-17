package space.kovo.nocnyprud2.backend.services

import android.content.Context
import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import space.kovo.nocnyprud2.backend.alarms.TariffAlarmScheduler
import org.greenrobot.eventbus.EventBus
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity
import space.kovo.nocnyprud2.backend.events.ProviderApiEvent
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.database.TimetableRepository
import space.kovo.nocnyprud2.backend.repositories.database.TimetableRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepository
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepositoryImpl
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject
import space.kovo.nocnyprud2.backend.services.httpService.HttpService
import space.kovo.nocnyprud2.backend.services.httpService.HttpServiceImpl
import space.kovo.nocnyprud2.backend.utils.ReflectionUtils

class TimetableServiceImpl private constructor(
    private val httpService: HttpService,
    private val servicePointRepository: ServicePointRepository,
    private val timetableRepository: TimetableRepository,
    private val settingsStorageRepository: SettingsStorageRepository
) : TimetableService {

    companion object {

        /**
         *  ČEZ publishes about a week ahead, so topping up with two days to spare leaves plenty
         *  of room for a device that was offline or asleep for a while.
         */
        const val REFRESH_WHEN_LESS_THAN_SECONDS_LEFT = 2 * 24 * 3600L

        @Volatile
        private var instance: TimetableService? = null

        fun getInstance(): TimetableService {
            return instance ?: synchronized(this) {
                instance ?: TimetableServiceImpl(
                    HttpServiceImpl.getInstance(),
                    ServicePointRepositoryImpl.getInstance(),
                    TimetableRepositoryImpl.getInstance(),
                    SettingsStorageRepositoryImpl.getInstance(),
                ).also { instance = it }
            }
        }
    }

    override fun acquireDataFromProvider(): List<TimetableEntity> {
        val httpRequestObject: HttpRequestObject = ReflectionUtils.getHttpRequestObject()
        val response: String = httpService.perform(httpRequestObject)
        return ReflectionUtils.getHttpResponseHandler().onSuccess(response)
    }

    override fun saveAndReplaceTimetable(data: List<TimetableEntity>) {
        runBlocking {
            val servicePointId = servicePointRepository.getOrCreateDefaultServicePoint().uid
            timetableRepository.replaceTimetables(servicePointId, data)
        }
        // announced only once the rows are actually in the database - listeners read them back
        // from there, and announcing earlier made them race the write
        EventBus.getDefault().post(ProviderApiEvent(
            ProviderApiEvent.EventType.TIMESPANS_QUERIED_PARSED_AND_SAVED))
    }

    override suspend fun isTimetableRunningOut(): Boolean {

        if (!servicePointRepository.isDefaultServicePointSetUp()) {
            return false
        }

        val servicePointId = servicePointRepository.getOrCreateDefaultServicePoint().uid
        val latestEnd = timetableRepository.getTimetables(servicePointId)
            .maxOfOrNull { it.sequenceEnd }
            ?: return true

        val runsOutWithin = latestEnd - (System.currentTimeMillis() / 1000)

        Logger.d("Stored timetable runs out in ${runsOutWithin / 3600} hours")

        return runsOutWithin < REFRESH_WHEN_LESS_THAN_SECONDS_LEFT
    }

    override suspend fun refreshTimetable(context: Context): Boolean {
        return try {
            saveAndReplaceTimetable(acquireDataFromProvider())
            // the newly stored times may differ from the ones the pending alarm was armed for
            TariffAlarmScheduler.getInstance().rearmFromStoredTimetable(context)
            Logger.i("Timetable refreshed from provider")
            true
        } catch (e: Exception) {
            Logger.e(e, "Timetable refresh failed")
            false
        }
    }
}
