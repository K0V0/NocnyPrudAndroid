package space.kovo.nocnyprud2.backend.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.notifications.TariffNotifications

/**
 *  Fired by the armed exact alarm shortly before the tariff flips: tells the user, then arms the
 *  alarm for the boundary after this one.
 */
class TariffAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_BOUNDARY_EPOCH = "boundary_epoch"
        const val EXTRA_BOUNDARY_TYPE = "boundary_type"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val epochSeconds = intent.getLongExtra(EXTRA_BOUNDARY_EPOCH, 0)
        val type = intent.getStringExtra(EXTRA_BOUNDARY_TYPE)
            ?.let { runCatching { TariffBoundary.Type.valueOf(it) }.getOrNull() }

        Logger.i("Tariff alarm fired for $type at $epochSeconds")

        if (type != null && epochSeconds > 0) {
            TariffNotifications.notifyAboutBoundary(
                context, TariffBoundary(epochSeconds, type))
        }

        // onReceive must not block, and re-arming needs to read the database
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                TariffAlarmScheduler.getInstance().rearmFromStoredTimetable(context)
            } catch (e: Exception) {
                Logger.e(e, "Could not arm the next tariff alarm")
            } finally {
                pendingResult.finish()
            }
        }
    }
}
