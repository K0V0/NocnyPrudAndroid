package space.kovo.nocnyprud2.backend.repositories.database

import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity

interface TimetableRepository {

    suspend fun replaceTimetables(servicePointId: Int, entities: List<TimetableEntity>)

    suspend fun getTimetables(servicePointId: Int): List<TimetableEntity>
}
