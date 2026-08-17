package space.kovo.nocnyprud2.backend.utils

import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepositoryImpl
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject
import space.kovo.nocnyprud2.backend.services.httpService.HttpResponseHandler
import java.lang.reflect.InvocationTargetException

class ReflectionUtils {

    companion object {

        private const val HTTP_REQUEST_OBJECTS_PACKAGE_PATH = "space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates"
        private const val HTTP_REQUEST_OBJECT_CLASS_NAME = "HttpRequestObjectImpl"
        private const val HTTP_RESPONSE_HANDLER_CLASS_NAME = "HttpResponseHandlerImpl"

        /**
         *  Instantiates [className] using its first usable constructor.
         *  The last failure is kept so that the real cause can be reported instead of
         *  a bare "failed to load" - the underlying exception is what actually explains
         *  the problem (missing setup data, broken response, ...).
         */
        inline fun <reified T> loadClassByName(className: String): Pair<T?, Throwable?> {
            return try {
                val clazz = Class.forName(className).kotlin
                val constructors = clazz.constructors
                Logger.d("Loading $className constructors $constructors")
                var result: T? = null
                var lastError: Throwable? = null
                constructors.forEach {
                    if (result == null) {
                        try {
                            result = it?.call() as T
                        } catch (e: Throwable) {
                            // reflective calls wrap the real problem, unwrap it for the log
                            val cause = (e as? InvocationTargetException)?.targetException ?: e
                            lastError = cause
                            Logger.e(cause, "Constructor ${it.name} of $className failed")
                        }
                    }
                }
                Pair(result, lastError)
            } catch (e: Throwable) {
                Logger.e(e, "Failed to load class $className")
                Pair(null, e)
            }
        }

        fun getCurrentProviderSpecificPath(): String {
            return runBlocking {
                SettingsStorageRepositoryImpl.getInstance().getServicePointCountry() +
                        "." +
                        SettingsStorageRepositoryImpl.getInstance().getServicePointProvider()
            }
        }

        fun getHttpRequestObject(): HttpRequestObject {
            val className = HTTP_REQUEST_OBJECTS_PACKAGE_PATH + "." +
                    getCurrentProviderSpecificPath() + "." + HTTP_REQUEST_OBJECT_CLASS_NAME
            val (result, error) = loadClassByName<HttpRequestObject>(className)
            return result ?: throw IllegalStateException("Failed to load $className", error)
        }

        fun getHttpResponseHandler(): HttpResponseHandler {
            val className = HTTP_REQUEST_OBJECTS_PACKAGE_PATH + "." +
                    getCurrentProviderSpecificPath() + "." + HTTP_RESPONSE_HANDLER_CLASS_NAME
            val (result, error) = loadClassByName<HttpResponseHandler>(className)
            return result ?: throw IllegalStateException("Failed to load $className", error)
        }
    }
}
