package space.kovo.nocnyprud2.backend.services

import space.kovo.nocnyprud2.backend.daos.ServicePointDao
import space.kovo.nocnyprud2.backend.singletons.DB

class ServicePointServiceImpl: ServicePointService {

    // ok, it just lazy loading reference to singleton ROOM database instance
    val servicePointDao: ServicePointDao by lazy { DB.instance!!.servicePointDao() }

    override fun createDefaultServicePointIfNoneExists() {
        //TODO (future) - usecase when users remvoe all its accounts, detect that fact and offer different resolution
        // than first run setup wizard
        if (servicePointDao.count() == 0L) {
            servicePointDao.createDefault()
        }
    }
}
