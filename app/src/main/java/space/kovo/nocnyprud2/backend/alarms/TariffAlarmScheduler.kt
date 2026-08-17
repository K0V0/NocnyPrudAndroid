package space.kovo.nocnyprud2.backend.alarms

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.database.TimetableRepositoryImpl

/**
 *  Arms a single exact alarm for the next tariff boundary.
 *
 *  Only ever one alarm is pending: when it fires it arms the following one. Queueing the whole
 *  week up front would waste alarm slots and, more importantly, would go stale - ČEZ republishes
 *  different switching times from one week to the next, so every refresh re-arms from scratch.
 */
class TariffAlarmScheduler {

    companion object {

        /** how long before the boundary the user gets told */
        const val LEAD_TIME_SECONDS = 15 * 60L

        private const val REQUEST_CODE = 100

        fun getInstance(): TariffAlarmScheduler = TariffAlarmScheduler()
    }

    /**
     *  Re-arms from whatever is currently stored. Safe to call repeatedly - it always cancels the
     *  previous alarm first, so a moved boundary can never leave a stale one behind.
     */
    suspend fun rearmFromStoredTimetable(context: Context) {

        val servicePointId = ServicePointRepositoryImpl.getInstance()
            .getOrCreateDefaultServicePoint().uid
        val entities = TimetableRepositoryImpl.getInstance().getTimetables(servicePointId)

        val nowSeconds = System.currentTimeMillis() / 1000
        val next = TariffBoundary.fromTimetable(entities)
            .firstOrNull { boundary -> boundary.epochSeconds - LEAD_TIME_SECONDS > nowSeconds }

        cancel(context)

        if (next == null) {
            Logger.w("No upcoming tariff boundary to arm an alarm for, stored timetable ran out")
            return
        }
        arm(context, next)
    }

    private fun arm(context: Context, boundary: TariffBoundary) {

        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        val triggerAtMillis = (boundary.epochSeconds - LEAD_TIME_SECONDS) * 1000

        if (!canScheduleExactAlarms(context)) {
            // still better than nothing: an inexact alarm may be delayed by Doze, but the user
            // who declined the permission gets a late notification rather than none at all
            Logger.w("Exact alarms not permitted, falling back to an inexact alarm")
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent(context, boundary))
            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent(context, boundary))

        Logger.i("Armed tariff alarm for ${boundary.type} at ${boundary.epochSeconds}, " +
                "firing at ${triggerAtMillis / 1000}")
    }

    fun cancel(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java) ?: return
        alarmManager.cancel(cancellationIntent(context))
    }

    fun canScheduleExactAlarms(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return true
        }
        return context.getSystemService(AlarmManager::class.java)?.canScheduleExactAlarms() ?: false
    }

    private fun pendingIntent(context: Context, boundary: TariffBoundary): PendingIntent {
        val intent = Intent(context, TariffAlarmReceiver::class.java).apply {
            putExtra(TariffAlarmReceiver.EXTRA_BOUNDARY_EPOCH, boundary.epochSeconds)
            putExtra(TariffAlarmReceiver.EXTRA_BOUNDARY_TYPE, boundary.type.name)
        }
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    /**
     *  Cancelling matches on the intent only, so the extras of the armed alarm are irrelevant.
     */
    private fun cancellationIntent(context: Context): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            Intent(context, TariffAlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
}
