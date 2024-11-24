package space.kovo.nocnyprud2.backend.entities.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 *  DATA MODEL description
 *
 *  One line in SQLite database - one time sequence with low price current for a given day.
 *
 *  The vision is in some (distant) future user will be able to click given sequence in his timetable view
 *  and set up some action for its duration.
 *
 *  This data structure comes to my mind to handle the pain that  may occur if provider make changes in timetables
 *  that have already been published and user already have some actions set up for given time sequence - this way it
 *  should be relatively easy to find out "conflicting" sequences just comparing your data <-> provider data
 */

@Entity(
    tableName = "timetable",
    foreignKeys = [ForeignKey(
        entity = ServicePointEntity::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("servicePointId"),
        onDelete = ForeignKey.CASCADE
    )],
//    indices = [
//        androidx.room.Index(value = ["sequence_date"])
//    ]
)
data class TimetableEntity(

    @PrimaryKey
    val id: Int,

    @ColumnInfo
    val servicePointId: Int,

//    @ColumnInfo
//    val sequenceDate: String,

    @ColumnInfo
    val sequenceStart: Long,

    @ColumnInfo
    val sequenceEnd: Long,
)
