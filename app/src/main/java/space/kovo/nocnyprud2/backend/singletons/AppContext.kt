package space.kovo.nocnyprud2.backend.singletons

import android.content.Context

/**
 *  Application context for the parts of the backend that need one without being handed it,
 *  such as EventBus subscribers. Always the application context, never an activity, so nothing
 *  here can leak a screen.
 */
object AppContext {
    var instance: Context? = null
}
