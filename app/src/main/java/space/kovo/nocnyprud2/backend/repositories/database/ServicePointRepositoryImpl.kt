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

    override suspend fun isDefaultServicePointSetUp(): Boolean {
        // deliberately NOT getOrCreate: merely asking the question must not create a row
        val entity: ServicePointEntity = servicePointDao.getDefault() ?: return false

        // country and provider are stored as soon as they are picked, so they alone would also
        // report a wizard the user walked out of halfway - only the provider form data, which is
        // written by the very last step, means the wizard actually ran to the end
        val setUp = !entity.countryCode.isNullOrBlank() &&
                !entity.providerCode.isNullOrBlank() &&
                hasProviderFormData(entity.providerFormsContent)

        Logger.d("Default service point set up: $setUp, entity: $entity")

        return setUp
    }

    private fun hasProviderFormData(providerFormsContent: String?): Boolean {
        // an empty JSON object is what an unfilled form serializes to
        return !providerFormsContent.isNullOrBlank() && providerFormsContent.trim() != "{}"
    }

    override suspend fun getProviderDataForDefaultServicePoint(): String {
        return getOrCreateDefaultServicePoint().providerFormsContent
            ?: throw IllegalStateException(
                "Provider form data for the default service point is not set up yet, " +
                        "the setup wizard has to be completed first")
    }

    override suspend fun setCountryForDefaultServicePoint(countryCode: String) {

        Logger.d("Setting country for default service point entity, result: $countryCode")

        servicePointDao.createDefault()
        servicePointDao.updateDefaultCountry(countryCode)
    }

    override suspend fun setProviderForDefaultServicePoint(providerCode: String) {

        Logger.d("Setting energy provider for default service point entity, result: $providerCode")

        servicePointDao.createDefault()
        servicePointDao.updateDefaultProvider(providerCode)
    }

    override suspend fun setProviderDataForDefaultServicePoint(providerData: String) {

        Logger.d("Saving provider form data for default service point entity, result: $providerData")

        servicePointDao.createDefault()
        servicePointDao.updateDefaultProviderFormData(providerData)
    }
}
