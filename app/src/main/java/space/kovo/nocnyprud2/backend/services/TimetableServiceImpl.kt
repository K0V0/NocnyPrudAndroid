package space.kovo.nocnyprud2.backend.services

import kotlinx.coroutines.runBlocking
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
}
