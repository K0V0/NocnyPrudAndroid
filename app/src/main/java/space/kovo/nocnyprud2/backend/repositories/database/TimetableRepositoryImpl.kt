package space.kovo.nocnyprud2.backend.repositories.database

import space.kovo.nocnyprud2.backend.daos.TimetableDao
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity
import space.kovo.nocnyprud2.backend.singletons.Database

class TimetableRepositoryImpl : TimetableRepository {

    companion object {

        @Volatile
        private var instance: TimetableRepository? = null

        fun getInstance(): TimetableRepository {
            return instance ?: synchronized(this) {
                instance ?: TimetableRepositoryImpl().also { instance = it }
            }
        }
    }

    // ok, it just lazy loading reference to singleton ROOM database instance
    val timetableDao: TimetableDao by lazy { Database.instance!!.timetableDao() }

    override suspend fun replaceTimetables(servicePointId: Int, entities: List<TimetableEntity>) {
        timetableDao.deleteAllTimetablesForServicePoint(servicePointId)
        entities.forEach { timetableEntity ->
            timetableDao.createTimetableSequence(
                servicePointId, timetableEntity.sequenceStart, timetableEntity.sequenceEnd)
        }
    }
}
