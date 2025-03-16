package space.kovo.nocnyprud2.backend.daos

import androidx.room.Dao
import androidx.room.Query
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity

@Dao
interface TimetableDao {

    @Query("INSERT INTO timetable (servicePointId, sequenceStart, sequenceEnd) " +
            "VALUES (:uid, :sequenceStart, :sequenceEnd)")
    suspend fun createTimetableSequence(uid: Int, sequenceStart: Long, sequenceEnd: Long)

    @Query("SELECT * FROM timetable WHERE servicepointid LIKE :uid ORDER BY sequencestart")
    suspend fun getServicePointTimetables(uid: Int): List<TimetableEntity>

    @Query("DELETE FROM timetable WHERE servicepointid LIKE :uid")
    suspend fun deleteAllTimetablesForServicePoint(uid: Int)

}
