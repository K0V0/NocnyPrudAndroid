package space.kovo.nocnyprud2.backend.services

class WizardServiceImpl : WizardService {

    //TODO Still in 2024 IOC container/dependency injection not present in android
    // resolve this cyka somehow (libs exists) blyat
    val servicePointService: ServicePointService = ServicePointServiceImpl()

    override fun performStartupActions() {
        servicePointService.createDefaultServicePointIfNoneExists()
    }
}
