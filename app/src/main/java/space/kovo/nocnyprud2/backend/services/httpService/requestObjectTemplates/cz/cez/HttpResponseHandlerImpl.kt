package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez

import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez.HdoDataResponse
import space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez.HdoDataResultPrint
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity
import space.kovo.nocnyprud2.backend.services.httpService.HttpResponseHandler
import space.kovo.nocnyprud2.backend.utils.fromJson
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 *  Turns the ČEZ weekly HDO table into dated low tariff sequences.
 *
 *  The API answers with one row per weekday rather than with dates. That pattern is NOT stable -
 *  ČEZ does republish different times from one week to the next - so it is expanded here into
 *  concrete dates for the days ahead and stored as ordinary timestamps, exactly like the dated
 *  EAN endpoint used to deliver. Refreshing simply overwrites them.
 */
class HttpResponseHandlerImpl : HttpResponseHandler {

    companion object {

        /** how many days ahead the weekly pattern gets expanded into */
        const val DAYS_AHEAD = 7

        val timeZone: TimeZone = TimeZone.getTimeZone("Europe/Prague")

        /**
         *  Czech weekday labels as returned by the API. The last row covers Sundays *and* public
         *  holidays, but the API gives us no holiday calendar, so holidays falling on a weekday
         *  still get their weekday times here.
         */
        private val DAY_NAME_TO_CALENDAR_DAY: Map<String, Int> = mapOf(
            "pondělí" to Calendar.MONDAY,
            "úterý" to Calendar.TUESDAY,
            "středa" to Calendar.WEDNESDAY,
            "čtvrtek" to Calendar.THURSDAY,
            "pátek" to Calendar.FRIDAY,
            "sobota" to Calendar.SATURDAY,
            "neděle" to Calendar.SUNDAY
        )

        private val INTERVAL = Regex("^\\s*(\\d{1,2}):(\\d{2})\\s*-\\s*(\\d{1,2}):(\\d{2})\\s*$")

        fun calendarDayOf(dayLabel: String): Int? {
            val normalized = dayLabel.trim().lowercase(Locale.ROOT)
            // "Neděle a svátky" carries a suffix, so match on how the label starts
            return DAY_NAME_TO_CALENDAR_DAY.entries
                .firstOrNull { (name, _) -> normalized.startsWith(name) }
                ?.value
        }
    }

    override fun onSuccess(data: String): List<TimetableEntity> {

        val response = fromJson<HdoDataResponse>(data)

        response.errors?.takeIf { it.isNotEmpty() }?.let { errors ->
            throw IllegalStateException(
                "ČEZ rejected the query: ${errors.mapNotNull { it.message }}")
        }

        val entry = pickResultEntry(response)
        val rows = entry.rows.orEmpty().filter { !it.day.isNullOrBlank() }

        if (rows.isEmpty()) {
            throw IllegalStateException("ČEZ returned no switching times for this HDO code")
        }

        // weekday -> the intervals published for it
        val intervalsPerDay: Map<Int, List<String>> = rows
            .mapNotNull { row ->
                calendarDayOf(row.day!!)?.let { day -> day to row.intervals.orEmpty() }
            }
            .toMap()

        val result = mutableListOf<TimetableEntity>()
        val day = Calendar.getInstance(timeZone).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        repeat(DAYS_AHEAD) {
            val intervals = intervalsPerDay[day.get(Calendar.DAY_OF_WEEK)].orEmpty()

            intervals.forEach { interval ->
                toTimetableEntity(day, interval)?.let { result.add(it) }
                    ?: Logger.w("Skipping unparsable ČEZ interval '$interval'")
            }
            day.add(Calendar.DAY_OF_MONTH, 1)
        }

        return mergeAdjacent(result)
    }

    /**
     *  A generic "kód" can match several command codes at once, each with its own tariff and its
     *  own times - picking one at random would silently give the user a wrong timetable, so an
     *  ambiguous answer is reported instead.
     */
    private fun pickResultEntry(response: HdoDataResponse): HdoDataResultPrint {
        val entries = response.data?.hdoData?.resultPrint.orEmpty()

        if (entries.isEmpty()) {
            throw IllegalStateException(
                "ČEZ knows no HDO code like this in the selected region")
        }
        if (entries.size == 1) {
            return entries.first()
        }

        val candidates = entries.mapNotNull { it.povel }.joinToString(", ")
        throw IllegalStateException(
            "This code matches ${entries.size} different schedules ($candidates), " +
                    "please enter your exact HDO command code")
    }

    /**
     *  Builds one sequence from a "0:00 - 2:55" style interval on the given day.
     *  24:00 is a valid end in this data and means midnight at the end of that day.
     */
    private fun toTimetableEntity(day: Calendar, interval: String): TimetableEntity? {
        val match = INTERVAL.matchEntire(interval) ?: return null

        val startHour = match.groupValues[1].toInt()
        val startMinute = match.groupValues[2].toInt()
        val endHour = match.groupValues[3].toInt()
        val endMinute = match.groupValues[4].toInt()

        val start = atTime(day, startHour, startMinute) ?: return null
        val end = atTime(day, endHour, endMinute) ?: return null

        if (end <= start) {
            return null
        }

        return TimetableEntity(
            id = 0,
            servicePointId = 0,
            sequenceStart = start,
            sequenceEnd = end
        )
    }

    private fun atTime(day: Calendar, hour: Int, minute: Int): Long? {
        if (hour > 24 || minute > 59) {
            return null
        }
        val moment = (day.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            // handles 24:00 by rolling into the next day
            add(Calendar.HOUR_OF_DAY, hour)
            add(Calendar.MINUTE, minute)
        }
        return moment.timeInMillis / 1000
    }

    /**
     *  Joins sequences that touch or overlap - most importantly the nightly pair that a day
     *  boundary splits into "22:50 - 24:00" plus the next day's "0:00 - 2:55".
     */
    private fun mergeAdjacent(entities: List<TimetableEntity>): List<TimetableEntity> {
        val sorted = entities.sortedBy { it.sequenceStart }
        val merged = mutableListOf<TimetableEntity>()

        sorted.forEach { current ->
            val previous = merged.lastOrNull()

            if (previous != null && current.sequenceStart <= previous.sequenceEnd) {
                merged[merged.size - 1] = previous.copy(
                    sequenceEnd = maxOf(previous.sequenceEnd, current.sequenceEnd))
            } else {
                merged.add(current)
            }
        }

        return merged
    }
}
