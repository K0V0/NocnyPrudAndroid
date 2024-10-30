package space.kovo.nocnyprud2.backend.inits

import android.content.Context
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


class LoggerInit : Init {

    override fun init(context: Context) {
        Logger.addLogAdapter(AndroidLogAdapter())
    }
}
