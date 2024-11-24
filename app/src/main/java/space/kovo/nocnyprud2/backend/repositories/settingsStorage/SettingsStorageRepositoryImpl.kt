package space.kovo.nocnyprud2.backend.repositories.settingsStorage

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.first
import space.kovo.nocnyprud2.backend.singletons.SettingsStorage
import kotlin.reflect.KClass

class SettingsStorageRepositoryImpl : SettingsStorageRepository {

    companion object {
        private const val SERVICE_POINT: String = "service_point"
        private val SERVICE_POINT_COUNTRY_CODE = SERVICE_POINT + "_country_code"
        private val SERVICE_POINT_PROVIDER_CODE = SERVICE_POINT + "_provider_code"
        private val SERVICE_POINT_PROVIDER_FORM_DATA = SERVICE_POINT + "_provider_form_data"

        private val keysMap: Map<String, KClass<*>> = mapOf(
            Pair(SERVICE_POINT_COUNTRY_CODE, String::class),
            SERVICE_POINT_PROVIDER_CODE to String::class,
            SERVICE_POINT_PROVIDER_FORM_DATA to String::class
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

    override suspend fun getServicePointCountry(): String {
        return getString(SERVICE_POINT_COUNTRY_CODE) ?: ""
    }

    override suspend fun setServicePointProvider(providerCode: String) {
        putString(SERVICE_POINT_PROVIDER_CODE, providerCode)
    }

    override suspend fun getServicePointProvider(): String {
        return getString(SERVICE_POINT_PROVIDER_CODE) ?: ""
    }

    override suspend fun setServicePointProviderFormData(providerCode: String) {
        return putString(SERVICE_POINT_PROVIDER_FORM_DATA, providerCode)
    }

    override suspend fun getServicePointProviderFormData(): String {
        return getString(SERVICE_POINT_PROVIDER_FORM_DATA) ?: "{}"
    }

    override suspend fun getAll(): List<*> {
        return keysMap.map {
            pair -> when (pair.value) {
                String::class -> getString(pair.key)
                Int::class -> getInt(pair.key)
                else -> getString(pair.key)
            }
        }
    }



    private suspend fun <T> putValue(key: Preferences.Key<T>, value: T) {
        Logger.d("Putting value into settings storage, key: $key, value: $value")
        SettingsStorage.data?.edit { preferences ->
            preferences[key] = value
        }
    }

    private suspend fun <T> getValue(key: Preferences.Key<T>): T? {
        val value: T? = SettingsStorage.data?.data?.first()?.get(key)
        Logger.d("Getting value from settings storage, key: $key, value : $value")
        return value
    }

}
