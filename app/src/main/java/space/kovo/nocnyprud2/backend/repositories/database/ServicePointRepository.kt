package space.kovo.nocnyprud2.backend.repositories.database

import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity

interface ServicePointRepository {

    suspend fun getOrCreateDefaultServicePoint(): ServicePointEntity

    /**
     *  Whether the setup wizard has been carried all the way through for the default service
     *  point, i.e. whether the app has everything it needs to query the provider.
     */
    suspend fun isDefaultServicePointSetUp(): Boolean

    suspend fun getProviderDataForDefaultServicePoint(): String

    suspend fun setCountryForDefaultServicePoint(countryCode: String)

    suspend fun setProviderForDefaultServicePoint(providerCode: String)

    suspend fun setProviderDataForDefaultServicePoint(providerData: String)

}
