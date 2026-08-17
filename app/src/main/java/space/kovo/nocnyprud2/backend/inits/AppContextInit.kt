package space.kovo.nocnyprud2.backend.inits

import android.content.Context
import space.kovo.nocnyprud2.backend.singletons.AppContext

class AppContextInit : Init {

    override fun init(context: Context) {
        AppContext.instance = context.applicationContext
    }
}
