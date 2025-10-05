package space.kovo.nocnyprud2.backend.dtos.timetable

data class TimetableDayDto(
    var date: String = "",
    var timespans: List<TimespanDto> = ArrayList()
)
