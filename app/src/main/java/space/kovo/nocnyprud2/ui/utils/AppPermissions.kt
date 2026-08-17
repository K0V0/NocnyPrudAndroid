package space.kovo.nocnyprud2.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import space.kovo.nocnyprud2.backend.alarms.TariffAlarmScheduler
import space.kovo.nocnyprud2.backend.notifications.TariffNotifications

/**
 *  The three things the app needs from the user to be able to warn about the tariff while it is
 *  not open, each of them granted in a different way.
 */
class AppPermissions {

    companion object {

        fun canPostNotifications(context: Context): Boolean =
            TariffNotifications.canPostNotifications(context)

        fun canScheduleExactAlarms(context: Context): Boolean =
            TariffAlarmScheduler.getInstance().canScheduleExactAlarms(context)

        /**
         *  Whether the system will let the app run in the background unthrottled. Manufacturers
         *  like Motorola, Xiaomi or Samsung are far more aggressive here than stock Android, and
         *  this is the usual reason background notifications quietly stop arriving.
         */
        fun isIgnoringBatteryOptimizations(context: Context): Boolean {
            val powerManager = context.getSystemService(PowerManager::class.java) ?: return true
            return powerManager.isIgnoringBatteryOptimizations(context.packageName)
        }

        fun allGranted(context: Context): Boolean =
            canPostNotifications(context) &&
                    canScheduleExactAlarms(context) &&
                    isIgnoringBatteryOptimizations(context)

        /**
         *  Settings screen where the user can allow exact alarms. Only reachable since API 31,
         *  below that the permission does not exist and is implicitly granted.
         */
        fun exactAlarmSettingsIntent(context: Context): Intent? {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                return null
            }
            return Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                .setData(Uri.fromParts("package", context.packageName, null))
        }

        /**
         *  Opens the battery optimisation list rather than asking directly: the direct
         *  ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS dialog needs a permission whose use is
         *  restricted on the Play Store.
         */
        fun batteryOptimizationSettingsIntent(): Intent =
            Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)

        fun notificationSettingsIntent(context: Context): Intent =
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
}
