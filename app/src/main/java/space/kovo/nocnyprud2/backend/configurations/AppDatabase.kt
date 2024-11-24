package space.kovo.nocnyprud2.backend.configurations

import androidx.room.Database
import androidx.room.RoomDatabase
import space.kovo.nocnyprud2.backend.daos.ServicePointDao
import space.kovo.nocnyprud2.backend.daos.TimetableDao
import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity

@Database(
    entities = [
        ServicePointEntity::class,
        TimetableEntity::class,
    ],
    version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun servicePointDao(): ServicePointDao
    abstract fun timetableDao(): TimetableDao
}
