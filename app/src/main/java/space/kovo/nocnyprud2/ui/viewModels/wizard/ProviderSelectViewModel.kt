package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepository
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.yamlValues.YamlValuesRepository
import space.kovo.nocnyprud2.backend.repositories.yamlValues.YamlValuesRepositoryImpl

class ProviderSelectViewModel : ViewModel() {

    companion object {
        //TODO dependency injection somehow
        private val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()
        private val settingsStorageRepository: SettingsStorageRepository = SettingsStorageRepositoryImpl()
        private val yamlValuesRepository: YamlValuesRepository = YamlValuesRepositoryImpl()
    }

    private val _countryCode = MutableLiveData<String>()
    private val _providerCode = MutableLiveData<String>()
    private val _availableProviders = MediatorLiveData<List<ProviderYmlEntity>>()

    init {
        viewModelScope.launch {
            _countryCode.value = settingsStorageRepository.getServicePointCountry()
            _availableProviders.addSource(_countryCode) {
                countryCode -> _availableProviders.value = yamlValuesRepository.getAvailableServiceProviders(countryCode)
            }
        }
    }

    val countryCode: LiveData<String>
        get() = _countryCode

    val providerCode: LiveData<String>
        get() = _providerCode

    val availableProviders: LiveData<List<ProviderYmlEntity>>
        get() = _availableProviders

    fun updateProvider(newProviderCode: String) {
        _providerCode.value = newProviderCode
        viewModelScope.launch {
            servicePointRepository.setProviderForDefaultServicePoint(newProviderCode)
            settingsStorageRepository.setServicePointProvider(newProviderCode)
        }
    }
}
