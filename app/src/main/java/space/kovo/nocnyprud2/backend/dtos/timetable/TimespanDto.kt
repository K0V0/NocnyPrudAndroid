package space.kovo.nocnyprud2.backend.dtos.timetable

import java.time.LocalDateTime

data class TimespanDto(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
)