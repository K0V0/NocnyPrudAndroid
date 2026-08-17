package space.kovo.nocnyprud2.backend.mappers.timetable

import space.kovo.nocnyprud2.backend.dtos.timetable.TimespanDto
import space.kovo.nocnyprud2.backend.dtos.timetable.TimetableDayDto
import space.kovo.nocnyprud2.backend.dtos.timetable.TimetableDto
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity
import space.kovo.nocnyprud2.backend.mappers.Mapper
import space.kovo.nocnyprud2.backend.utils.TimeUtils
import space.kovo.nocnyprud2.backend.utils.TimeUtils.Companion.dateToDateString
import space.kovo.nocnyprud2.backend.utils.TimeUtils.Companion.epochSecondsToDate
import space.kovo.nocnyprud2.backend.utils.TimeUtils.Companion.epochSecondsToDateString
import space.kovo.nocnyprud2.backend.utils.TimeUtils.Companion.epochSecondsToLocalDateTime
import space.kovo.nocnyprud2.backend.utils.TimeUtils.Companion.getHourOfDayDecimal
import space.kovo.nocnyprud2.backend.utils.TimeUtils.Companion.getMidnightEpochSeconds
import java.util.Date

class TimetableMapper : Mapper<List<TimetableEntity>, TimetableDto> {

    override fun toEntity(dto: TimetableDto): List<TimetableEntity> {
        // not going to be implemented for that way
        return emptyList()
    }

    override fun toDto(entity: List<TimetableEntity>): TimetableDto {

        val timetableDto = TimetableDto()
        val daysMap = LinkedHashMap<String, ArrayList<TimespanDto>>()

        // prepare - prefill which dates we have some sequence in
        for (e in entity) {
            daysMap.putIfAbsent(epochSecondsToDateString(e.sequenceStart), ArrayList())
            // a sequence ending exactly at midnight belongs entirely to the day before it,
            // registering its end date would add an empty day at the end of the timetable
            if (e.sequenceEnd > getMidnightEpochSeconds(e.sequenceEnd)) {
                daysMap.putIfAbsent(epochSecondsToDateString(e.sequenceEnd), ArrayList())
            }
        }

        // split sequences going through midnight in timezone of device user
        // fillup hours in decimal format
        for (e in entity) {
            if (TimeUtils.isSameDay(e.sequenceStart, e.sequenceEnd)) {
                val startTime = epochSecondsToLocalDateTime(e.sequenceStart)
                val endTime = epochSecondsToLocalDateTime(e.sequenceEnd)
                daysMap
                    .get(epochSecondsToDateString(e.sequenceStart))
                    ?.add(TimespanDto(
                        startTime,
                        endTime,
                        getHourOfDayDecimal(startTime),
                        getHourOfDayDecimal(endTime)))
            } else {
                val dateFirst = epochSecondsToDate(e.sequenceStart)
                val dateSecond = epochSecondsToDate(e.sequenceEnd)
                // the boundary is midnight at the START of the second day - taking the first
                // day's midnight put both halves a whole day off
                val midnightEpochSeconds = getMidnightEpochSeconds(dateSecond)
                val startTimeFirst = epochSecondsToLocalDateTime(e.sequenceStart)
                val endTimeFirst = epochSecondsToLocalDateTime(midnightEpochSeconds - 1)
                val startTimeSecond = epochSecondsToLocalDateTime(midnightEpochSeconds)
                val endTimeSecond = epochSecondsToLocalDateTime(e.sequenceEnd)
                daysMap
                    .get(dateToDateString(dateFirst))
                    ?.add(TimespanDto(
                        startTimeFirst,
                        endTimeFirst,
                        getHourOfDayDecimal(startTimeFirst),
                        24.0f))
                // nothing left to draw on the second day when the sequence ends at its midnight
                if (e.sequenceEnd > midnightEpochSeconds) {
                    daysMap
                        .get(dateToDateString(dateSecond))
                        ?.add(TimespanDto(
                            startTimeSecond,
                            endTimeSecond,
                            0.0f,
                            getHourOfDayDecimal(endTimeSecond)))
                }
            }
        }

        // fillup DTO
        for (day in daysMap.keys) {
            timetableDto.timetable.add(TimetableDayDto().apply {
                date = day
                timespans = daysMap.get(day)!!
            })
        }

        return timetableDto
    }
}