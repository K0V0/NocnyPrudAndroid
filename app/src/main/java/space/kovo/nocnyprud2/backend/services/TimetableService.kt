package space.kovo.nocnyprud2.backend.services

import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity

interface TimetableService {

    fun acquireDataFromProvider(): List<TimetableEntity>

    fun saveAndReplaceTimetable(data: List<TimetableEntity>)
}
