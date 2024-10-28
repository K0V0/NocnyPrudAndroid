package space.kovo.nocnyprud2.backend.inits

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import space.kovo.nocnyprud2.backend.singletons.SettingsStorage

class SettingsStorageInit : Init {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun init(context: Context) {
        SettingsStorage.data = context.dataStore
    }
}
