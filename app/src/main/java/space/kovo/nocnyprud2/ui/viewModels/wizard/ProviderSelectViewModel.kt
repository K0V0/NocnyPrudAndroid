package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.converters.yamlValues.YamlValuesConverter
import space.kovo.nocnyprud2.backend.dtos.yamlValues.ProviderYmlDto

class ProviderSelectViewModel : WizardViewModelBase() {

    private val _countryCode = MutableLiveData<String>()
    private val _providerCode = MutableLiveData<String>()
    private val _availableProviders = MediatorLiveData<List<ProviderYmlDto>>()

    init {
        viewModelScope.launch {
            _countryCode.value = settingsStorageRepository.getServicePointCountry()
            _providerCode.value = settingsStorageRepository.getServicePointProvider()
            _availableProviders.addSource(_countryCode) {
                countryCode -> _availableProviders.value = YamlValuesConverter.getProvidersLocalized(
                yamlValuesRepository.getAvailableServiceProviders(countryCode))
            }
        }
    }

    val countryCode: LiveData<String>
        get() = _countryCode

    val providerCode: LiveData<String>
        get() = _providerCode

    val availableProviders: LiveData<List<ProviderYmlDto>>
        get() = _availableProviders

    fun updateProvider(newProviderCode: String) {
        _providerCode.value = newProviderCode
        viewModelScope.launch {
            servicePointRepository.setProviderForDefaultServicePoint(newProviderCode)
            settingsStorageRepository.setServicePointProvider(newProviderCode)
        }
    }
}
