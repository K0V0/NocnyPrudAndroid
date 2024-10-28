package space.kovo.nocnyprud2

import android.app.Application
import space.kovo.nocnyprud2.backend.inits.*

class MainApplication : Application() {

    private val inits: List<Init> = listOf(
        SettingsStorageInit(),
        DatabaseInit(),
        YamlMapperInit(),
        YamlValuesInit()
    )

    override fun onCreate() {
        super.onCreate()
        runInits()
    }

    private fun runInits() {
        inits.forEach { init -> init.init(this) }
    }
}
