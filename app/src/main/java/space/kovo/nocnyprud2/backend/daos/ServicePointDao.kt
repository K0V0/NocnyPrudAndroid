package space.kovo.nocnyprud2.backend.daos

import androidx.room.Dao
import androidx.room.Query
import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity

@Dao
interface ServicePointDao {

    companion object {
        const val DEFAULT_NAME = "default"
    }

    @Query("INSERT INTO service_point (name) VALUES ('$DEFAULT_NAME')")
    suspend fun createDefault()

    @Query("SELECT * FROM service_point WHERE name LIKE '$DEFAULT_NAME' LIMIT 1")
    suspend fun getDefault(): ServicePointEntity?

    @Query("UPDATE service_point SET countrycode = :countryCode WHERE name LIKE '$DEFAULT_NAME'")
    suspend fun updateDefaultCountry(countryCode: String)

    @Query("UPDATE service_point SET providercode = :providerCode WHERE name LIKE '$DEFAULT_NAME'")
    suspend fun updateDefaultProvider(providerCode: String)

    @Query("SELECT * FROM service_point WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): ServicePointEntity?

    @Query("SELECT * FROM service_point")
    suspend fun getAll(): List<ServicePointEntity>

    @Query("SELECT COUNT(*) FROM service_point")
    suspend fun count(): Long
}
