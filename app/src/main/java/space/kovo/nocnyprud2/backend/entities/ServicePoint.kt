package space.kovo.nocnyprud2.backend.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ServicePoint(

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
