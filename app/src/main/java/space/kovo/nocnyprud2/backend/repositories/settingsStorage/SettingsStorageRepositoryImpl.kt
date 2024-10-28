package space.kovo.nocnyprud2.backend.repositories.settingsStorage

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import space.kovo.nocnyprud2.backend.singletons.SettingsStorage

class SettingsStorageRepositoryImpl : SettingsStorageRepository {

    companion object {
        private const val SERVICE_POINT: String = "service_point"
        private val SERVICE_POINT_COUNTRY_CODE = SERVICE_POINT + "country_code"
        private val SERVICE_POINT_PROVIDER_CODE = SERVICE_POINT + "provider_code"

        private val keysMap: List<String> = listOf(
            SERVICE_POINT_COUNTRY_CODE,
            SERVICE_POINT_PROVIDER_CODE,
        )
    }



    override suspend fun putString(key: String, value: String) {
        putValue(stringPreferencesKey(key), value)
    }

    override suspend fun putInt(key: String, value: Int) {
        putValue(intPreferencesKey(key), value)
    }

    override suspend fun getString(key: String): String? {
        return getValue(stringPreferencesKey(key))
    }

    override suspend fun getInt(key: String): Int? {
        return getValue(intPreferencesKey(key))
    }



    override suspend fun setServicePointCountry(countryCode: String) {
        putString(SERVICE_POINT_COUNTRY_CODE, countryCode)
    }

    override suspend fun getServicePointCountry(): String? {
        return getString(SERVICE_POINT_COUNTRY_CODE)
    }

    override suspend fun setServicePointProvider(providerCode: String) {
        putString(SERVICE_POINT_PROVIDER_CODE, providerCode)
    }

    override suspend fun getServicePointProvider(): String? {
        return getString(SERVICE_POINT_PROVIDER_CODE)
    }



    private suspend fun <T> putValue(key: Preferences.Key<T>, value: T) {
        SettingsStorage.data?.edit { preferences ->
            preferences[key] = value
        }
    }

    private suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        return SettingsStorage.data?.data?.first()?.get(key)
    }

}
