package space.kovo.nocnyprud2.backend.utils

import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepositoryImpl
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject

class ReflectionUtils {

    companion object {

        private const val HTTP_REQUEST_OBJECTS_PACKAGE_PATH = "space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates"
        private const val HTTP_REQUEST_OBJECT_CLASS_NAME = "HttpRequestObjectImpl"

        inline fun <reified T> loadClassByName(className: String): T? {
            return try {
                val clazz = Class.forName(className).kotlin
                val constructors = clazz.constructors
                Logger.d("Loading $className constructors $constructors")
                var result: T? = null
                constructors.forEach {
                    try {
                        result = it?.call() as T
                    } catch (e: Exception) {
                        Logger.d("Constructor ${it.name} skipped")
                    }
                }
                return result
            } catch (e: Exception) {
                println("Failed to load class $className: ${e.message}")
                return null
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
            val result = loadClassByName<HttpRequestObject>(
                HTTP_REQUEST_OBJECTS_PACKAGE_PATH + "." + getCurrentProviderSpecificPath() + "." + HTTP_REQUEST_OBJECT_CLASS_NAME)
            if (result == null) {
                throw Exception("Failed to load $HTTP_REQUEST_OBJECT_CLASS_NAME")
            }
            return result
        }
    }
}
