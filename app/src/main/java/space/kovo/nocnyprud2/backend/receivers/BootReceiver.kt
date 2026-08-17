package space.kovo.nocnyprud2.backend.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.alarms.TariffAlarmScheduler
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.workers.TimetableRefreshWorker

/**
 *  Alarms are forgotten across a reboot, so they have to be armed again from what is stored.
 *  WorkManager restores its own schedule, re-scheduling here is just belt and braces.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Logger.i("Boot completed, restoring background schedule")

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (ServicePointRepositoryImpl.getInstance().isDefaultServicePointSetUp()) {
                    TariffAlarmScheduler.getInstance().rearmFromStoredTimetable(context)
                    TimetableRefreshWorker.schedule(context)
                }
            } catch (e: Exception) {
                Logger.e(e, "Could not restore background schedule after boot")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
