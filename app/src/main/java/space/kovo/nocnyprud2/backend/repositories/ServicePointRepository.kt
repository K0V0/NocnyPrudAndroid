package space.kovo.nocnyprud2.backend.repositories

import space.kovo.nocnyprud2.backend.entities.ServicePointEntity

interface ServicePointRepository {

    suspend fun getOrCreateDefaultServicePoint(): ServicePointEntity

    suspend fun setCountryForDefaultServicePoint(countryCode: String)
}
