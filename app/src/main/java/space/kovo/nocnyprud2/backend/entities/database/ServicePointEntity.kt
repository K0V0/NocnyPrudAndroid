package space.kovo.nocnyprud2.backend.entities.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_point",
    indices = [
        androidx.room.Index(value = ["name"], unique = true)
    ]
)
data class ServicePointEntity(

    @PrimaryKey
    val uid: Int,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val countryCode: String?,

    @ColumnInfo
    val providerCode: String?,

    @ColumnInfo
    val providerFormsContent: String?
)
