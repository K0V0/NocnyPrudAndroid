package space.kovo.nocnyprud2.backend.dtos.timetable

data class TimetableDto(
    val timetable: MutableList<TimetableDayDto> = mutableListOf()
)
