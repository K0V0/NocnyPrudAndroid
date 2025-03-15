package space.kovo.nocnyprud2.backend.services

import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject
import space.kovo.nocnyprud2.backend.services.httpService.HttpService
import space.kovo.nocnyprud2.backend.services.httpService.HttpServiceImpl
import space.kovo.nocnyprud2.backend.utils.ReflectionUtils

class TimetableServiceImpl private constructor(private val httpService: HttpService) : TimetableService {

    companion object {

        @Volatile
        private var instance: TimetableService? = null

        fun getInstance(): TimetableService {
            return instance ?: synchronized(this) {
                instance ?: TimetableServiceImpl(
                    HttpServiceImpl.getInstance()
                ).also { instance = it }
            }
        }
    }

    override fun acquireDataFromProvider(): List<ServicePointEntity> {
        val httpRequestObject: HttpRequestObject = ReflectionUtils.getHttpRequestObject()
        httpService.perform(httpRequestObject)
        TODO("Not yet implemented")
    }
}
