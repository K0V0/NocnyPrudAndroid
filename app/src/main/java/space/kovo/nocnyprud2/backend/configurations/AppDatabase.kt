package space.kovo.nocnyprud2.backend.configurations

import androidx.room.Database
import androidx.room.RoomDatabase
import space.kovo.nocnyprud2.backend.daos.ServicePointDao
import space.kovo.nocnyprud2.backend.entities.ServicePointEntity

@Database(
    entities = [
        ServicePointEntity::class
    ],
    version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun servicePointDao(): ServicePointDao
}
