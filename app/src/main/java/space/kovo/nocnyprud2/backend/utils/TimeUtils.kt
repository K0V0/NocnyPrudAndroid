package space.kovo.nocnyprud2.backend.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.time.LocalDateTime
import java.util.Locale

class TimeUtils {

    companion object {

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        init {
            sdf.timeZone = TimeZone.getDefault()
        }

        /**
         *  Get milliseconds from epoch at midnight at given day in timezone of user's device
         */
        fun getMidnightEpochSeconds(date: Date): Long {
                val cal = Calendar.getInstance(TimeZone.getDefault())
                cal.time = date
                cal.set(Calendar.HOUR_OF_DAY, 0)
                cal.set(Calendar.MINUTE, 0)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                return cal.timeInMillis / 1000
        }

        /**
         *  Get epoch seconds at midnight at the beginning of the given day,
         *  based on the device's local timezone.
         */
        fun getMidnightEpochSeconds(epochSeconds: Long): Long {
            return getMidnightEpochSeconds(epochSecondsToDate(epochSeconds))
        }

        /**
         *  Compare if two dates are the same day in user's device timezone
         */
        fun isSameDay(date1: Date, date2: Date): Boolean {
            val cal1 = Calendar.getInstance(TimeZone.getDefault())
            cal1.time = date1
            val cal2 = Calendar.getInstance(TimeZone.getDefault())
            cal2.time = date2

            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        }

        /**
         *  Compare if two epochs are the same day in user's device timezone
         */
        fun isSameDay(epochSeconds1: Long, epochSeconds2: Long): Boolean {
            val cal1 = Calendar.getInstance(TimeZone.getDefault()).apply {
                timeInMillis = epochSeconds1 * 1000
            }
            val cal2 = Calendar.getInstance(TimeZone.getDefault()).apply {
                timeInMillis = epochSeconds2 * 1000
            }

            return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
        }

        /**
         *  Convert UNIX time (epoch) into Date
         */
        fun epochSecondsToDate(seconds: Long) : Date {
            return Date(seconds * 1000)
        }

        fun epochSecondsToDateString(seconds: Long) : String {
            return sdf.format(epochSecondsToDate(seconds))
        }

        fun dateToDateString(date: Date) : String {
            return sdf.format(date)
        }

        /**
         *  Convert UNIX time (epoch) into LocalDateTime
         */
        fun epochSecondsToLocalDateTime(seconds: Long): LocalDateTime {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            cal.timeInMillis = seconds * 1000
            return LocalDateTime.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1, // Calendar.MONTH is 0-based
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                cal.get(Calendar.MILLISECOND) * 1_000_000
            )
        }

        /**
         * Get the hour of the day as a decimal number (e.g., 13.5 for 13:30)
         * from epoch seconds, respecting the device's timezone.
         */
        fun getHourOfDayDecimal(epochSeconds: Long): Float {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            cal.timeInMillis = epochSeconds * 1000
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)
            val second = cal.get(Calendar.SECOND)
            return hour + (minute / 60f) + (second / 3600f)
        }

        /**
         * Get the hour of the day as a decimal number (e.g., 13.5 for 13:30)
         * from a LocalDateTime object.
         */
        fun getHourOfDayDecimal(localDateTime: LocalDateTime): Float {
            val hour = localDateTime.hour
            val minute = localDateTime.minute
            val second = localDateTime.second
            return hour + (minute / 60f) + (second / 3600f)
        }
    }
}