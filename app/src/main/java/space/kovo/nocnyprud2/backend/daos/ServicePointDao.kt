package space.kovo.nocnyprud2.backend.daos

import androidx.room.Dao
import androidx.room.Query
import space.kovo.nocnyprud2.backend.entities.ServicePoint

@Dao
interface ServicePointDao {

    companion object {
        const val DEFAULT_NAME = "default"
    }

    @Query("INSERT INTO servicepoint (name) VALUES ('$DEFAULT_NAME')")
    fun createDefault()

    @Query("SELECT * FROM servicepoint WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): ServicePoint?

    @Query("SELECT * FROM servicepoint")
    fun getAll(): List<ServicePoint>

    @Query("SELECT COUNT(*) FROM servicepoint")
    fun count(): Long
}
