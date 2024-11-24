package space.kovo.nocnyprud2.backend.entities.database

import androidx.room.Embedded
import androidx.room.Relation

data class ServicePointWithTimetables(

    @Embedded val servicePointEntity: ServicePointEntity,

    @Relation(
        parentColumn = "uid",
        entityColumn = "servicePointId"
    )
    val timetables: List<TimetableEntity>

)

