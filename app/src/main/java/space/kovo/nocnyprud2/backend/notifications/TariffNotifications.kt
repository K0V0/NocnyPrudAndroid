package space.kovo.nocnyprud2.backend.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.MainActivity
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.alarms.TariffBoundary
import space.kovo.nocnyprud2.backend.utils.TimeUtils
import java.text.SimpleDateFormat
import java.util.Locale

class TariffNotifications {

    companion object {

        const val CHANNEL_ID = "low_tariff"
        private const val NOTIFICATION_ID = 1

        private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        /**
         *  Required since API 26 - without a channel nothing is ever shown.
         */
        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                return
            }
            val channel = NotificationChannel(
                CHANNEL_ID,
                context.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.notification_channel_description)
            }
            context.getSystemService(NotificationManager::class.java)
                ?.createNotificationChannel(channel)
        }

        fun canPostNotifications(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                return NotificationManagerCompat.from(context).areNotificationsEnabled()
            }
            return ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        fun notifyAboutBoundary(context: Context, boundary: TariffBoundary) {

            if (!canPostNotifications(context)) {
                Logger.w("Not allowed to post notifications, skipping tariff notification")
                return
            }

            val at = timeFormat.format(TimeUtils.epochSecondsToDate(boundary.epochSeconds))
            val text = when (boundary.type) {
                TariffBoundary.Type.LOW_TARIFF_STARTS ->
                    context.getString(R.string.notification_low_tariff_starts, at)
                TariffBoundary.Type.LOW_TARIFF_ENDS ->
                    context.getString(R.string.notification_low_tariff_ends, at)
            }

            val openApp = android.app.PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK),
                android.app.PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(openApp)
                .setAutoCancel(true)
                .build()

            try {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
                Logger.i("Posted tariff notification: $text")
            } catch (e: SecurityException) {
                // the permission may be revoked between the check above and here
                Logger.e(e, "Not allowed to post the tariff notification")
            }
        }
    }
}
