package space.kovo.nocnyprud2.backend.dtos.timetable

import java.sql.Date

data class TimetableDayDto(
    val date: Date,
    val timespans: List<TimespanDto>
)
