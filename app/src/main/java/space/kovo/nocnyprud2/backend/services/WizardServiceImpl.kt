package space.kovo.nocnyprud2.backend.services

import space.kovo.nocnyprud2.backend.repositories.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.ServicePointRepositoryImpl

class WizardServiceImpl : WizardService {

    //TODO Still in 2024 IOC container/dependency injection not present in android
    // resolve this cyka somehow (libs exists) blyat
    val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()

    override suspend fun performStartupActions() {
        servicePointRepository.getOrCreateDefaultServicePoint()
    }
}
