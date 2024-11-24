package space.kovo.nocnyprud2

import android.app.Application
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import space.kovo.nocnyprud2.backend.eventsSubscribers.GlobalEventSubscribers
import space.kovo.nocnyprud2.backend.inits.*

class MainApplication : Application() {

    private val inits: List<Init> = listOf(
        LoggerInit(),
        SettingsStorageInit(),
        DatabaseInit(),
        YamlMapperInit(),
        YamlValuesInit()
    )

    override fun onCreate() {
        super.onCreate()
        runInits()
        registerGlobalEventHandlers()
    }

    private fun runInits() {
        inits.forEach { init -> init.init(this) }
        Logger.d("Inits executed")
    }

    private fun registerGlobalEventHandlers() {
        EventBus.getDefault().register(GlobalEventSubscribers)
        Logger.d("Global events subscribers registered")
    }
}
