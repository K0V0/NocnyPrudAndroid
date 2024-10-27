package space.kovo.nocnyprud2.backend.configurations

import androidx.room.Database
import androidx.room.RoomDatabase
import space.kovo.nocnyprud2.backend.daos.ServicePointDao
import space.kovo.nocnyprud2.backend.entities.ServicePoint

@Database(
    entities = [
        ServicePoint::class
    ],
    version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun servicePointDao(): ServicePointDao
}
