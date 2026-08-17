package space.kovo.nocnyprud2.backend.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.services.TimetableServiceImpl
import java.util.concurrent.TimeUnit

/**
 *  Keeps the stored timetable topped up while the app is not being used.
 *
 *  Timing is deliberately left to the system: a whole week is published at once, so being
 *  deferred into a Doze maintenance window costs nothing. What must NOT be left to the system
 *  is the notification itself - that is an exact alarm, see TariffAlarmScheduler.
 */
class TimetableRefreshWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    companion object {

        private const val WORK_NAME = "timetable_refresh"

        fun schedule(context: Context) {

            val request = PeriodicWorkRequestBuilder<TimetableRefreshWorker>(1, TimeUnit.DAYS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                // KEEP, so that an already scheduled cycle is not restarted on every app start
                ExistingPeriodicWorkPolicy.KEEP,
                request)

            Logger.d("Periodic timetable refresh scheduled")
        }
    }

    override suspend fun doWork(): Result {

        if (!ServicePointRepositoryImpl.getInstance().isDefaultServicePointSetUp()) {
            // nothing to refresh yet, the wizard has not been finished
            return Result.success()
        }

        val service = TimetableServiceImpl.getInstance()

        if (!service.isTimetableRunningOut()) {
            Logger.d("Stored timetable is still fresh enough, nothing to refresh")
            return Result.success()
        }

        // retry rather than fail: the provider may just be briefly unavailable, and there are
        // still days of stored timetable left to work from
        return if (service.refreshTimetable(applicationContext)) Result.success() else Result.retry()
    }
}
