package space.kovo.nocnyprud2.backend.repositories.database

import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.backend.daos.ServicePointDao
import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity
import space.kovo.nocnyprud2.backend.singletons.Database

class ServicePointRepositoryImpl: ServicePointRepository {

    companion object {

        @Volatile
        private var instance: ServicePointRepository? = null

        fun getInstance(): ServicePointRepository {
            return instance ?: synchronized(this) {
                instance ?: ServicePointRepositoryImpl().also { instance = it }
            }
        }
    }

    // ok, it just lazy loading reference to singleton ROOM database instance
    val servicePointDao: ServicePointDao by lazy { Database.instance!!.servicePointDao() }

    override suspend fun getOrCreateDefaultServicePoint(): ServicePointEntity {

        var entity: ServicePointEntity? = servicePointDao.getDefault()

        Logger.d("Trying to get default service point entity, result: $entity")

        if (entity != null) {
            return entity
        }
        servicePointDao.createDefault()
        entity = servicePointDao.getDefault()

        Logger.d("Nothing in database, trying to create default service point entity, result: $entity")

        return entity!!
    }

    override suspend fun getProviderDataForDefaultServicePoint(): String {
        return getOrCreateDefaultServicePoint().providerFormsContent!!
    }

    override suspend fun setCountryForDefaultServicePoint(countryCode: String) {

        Logger.d("Setting country for default service point entity, result: $countryCode")

        servicePointDao.updateDefaultCountry(countryCode)
    }

    override suspend fun setProviderForDefaultServicePoint(providerCode: String) {

        Logger.d("Setting energy provider for default service point entity, result: $providerCode")

        servicePointDao.updateDefaultProvider(providerCode)
    }

    override suspend fun setProviderDataForDefaultServicePoint(providerData: String) {

        Logger.d("Saving provider form data for default service point entity, result: $providerData")

        servicePointDao.updateDefaultProviderFormData(providerData)
    }
}
