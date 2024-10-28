package space.kovo.nocnyprud2.backend.repositories.database

import space.kovo.nocnyprud2.backend.daos.ServicePointDao
import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity
import space.kovo.nocnyprud2.backend.singletons.Database

class ServicePointRepositoryImpl: ServicePointRepository {

    // ok, it just lazy loading reference to singleton ROOM database instance
    val servicePointDao: ServicePointDao by lazy { Database.instance!!.servicePointDao() }

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
