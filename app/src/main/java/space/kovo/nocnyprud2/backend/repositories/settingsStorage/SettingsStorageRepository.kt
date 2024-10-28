package space.kovo.nocnyprud2.backend.repositories.settingsStorage

interface SettingsStorageRepository {

    suspend fun putString(key: String, value: String)
    suspend fun putInt(key: String, value: Int)
    suspend fun getString(key: String): String?
    suspend fun getInt(key: String): Int?

    suspend fun setServicePointCountry(countryCode: String)
    suspend fun getServicePointCountry(): String?

    suspend fun setServicePointProvider(providerCode: String)
    suspend fun getServicePointProvider(): String?
}
