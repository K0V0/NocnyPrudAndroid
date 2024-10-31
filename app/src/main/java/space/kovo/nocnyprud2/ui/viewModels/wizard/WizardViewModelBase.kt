package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.ViewModel
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepository
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.yamlValues.YamlValuesRepository
import space.kovo.nocnyprud2.backend.repositories.yamlValues.YamlValuesRepositoryImpl

abstract class WizardViewModelBase : ViewModel() {

    companion object {
        //TODO dependency injection somehow
        val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()
        val settingsStorageRepository: SettingsStorageRepository = SettingsStorageRepositoryImpl()
        val yamlValuesRepository: YamlValuesRepository = YamlValuesRepositoryImpl()
    }
}
