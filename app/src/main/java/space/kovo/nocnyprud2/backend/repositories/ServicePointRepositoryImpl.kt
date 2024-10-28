package space.kovo.nocnyprud2.backend.repositories

import space.kovo.nocnyprud2.backend.daos.ServicePointDao
import space.kovo.nocnyprud2.backend.entities.ServicePointEntity
import space.kovo.nocnyprud2.backend.singletons.DB

class ServicePointRepositoryImpl: ServicePointRepository {

    // ok, it just lazy loading reference to singleton ROOM database instance
    val servicePointDao: ServicePointDao by lazy { DB.instance!!.servicePointDao() }

    override suspend fun getOrCreateDefaultServicePoint(): ServicePointEntity {
        val entity: ServicePointEntity? = servicePointDao.getDefault()
        if (entity != null) {
            return entity
        }
        servicePointDao.createDefault()
        return servicePointDao.getDefault()!!
    }

    override suspend fun setCountryForDefaultServicePoint(countryCode: String) {
        servicePointDao.updateDefaultCountry(countryCode)
    }
}
