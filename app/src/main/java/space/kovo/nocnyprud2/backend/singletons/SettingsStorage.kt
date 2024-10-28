package space.kovo.nocnyprud2.backend.singletons

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

object SettingsStorage {
    var data: DataStore<Preferences>? = null
}
