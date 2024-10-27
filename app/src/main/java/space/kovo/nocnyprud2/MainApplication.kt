package space.kovo.nocnyprud2

import android.app.Application
import space.kovo.nocnyprud2.backend.inits.DBInit
import space.kovo.nocnyprud2.backend.inits.Init
import space.kovo.nocnyprud2.backend.inits.ValuesInit
import space.kovo.nocnyprud2.backend.inits.YamlMapperInit

class MainApplication : Application() {

    private val inits: List<Init> = listOf(
        DBInit(),
        YamlMapperInit(),
        ValuesInit()
    )

    override fun onCreate() {
        super.onCreate()
        runInits()
    }

    private fun runInits() {
        inits.forEach { init -> init.init(this) }
    }
}
