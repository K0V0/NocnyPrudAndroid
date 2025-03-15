package space.kovo.nocnyprud2.backend.repositories.database

import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity

interface ServicePointRepository {

    suspend fun getOrCreateDefaultServicePoint(): ServicePointEntity

    suspend fun getProviderDataForDefaultServicePoint(): String

    suspend fun setCountryForDefaultServicePoint(countryCode: String)

    suspend fun setProviderForDefaultServicePoint(providerCode: String)

    suspend fun setProviderDataForDefaultServicePoint(providerData: String)

}
