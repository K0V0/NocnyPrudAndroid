package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ServicePointSetupViewModel : WizardViewModelBase() {

    private val _countryCode = MutableLiveData<String>()
    private val _providerCode = MutableLiveData<String>()
    private val _formFragmentName = MediatorLiveData<String>()
    private val _formDataJson = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            _countryCode.value = settingsStorageRepository.getServicePointCountry()
            _providerCode.value = settingsStorageRepository.getServicePointProvider()
            _formFragmentName.addSource(_providerCode) {
                providerCode -> _formFragmentName.value = yamlValuesRepository
                    .getServicePointSetupLayout(_countryCode.value!!, providerCode)
            }
            _formFragmentName.addSource(_countryCode) {
                countryCode -> _formFragmentName.value = yamlValuesRepository
                    .getServicePointSetupLayout(countryCode, providerCode.value!!)
            }
            _formDataJson.value = settingsStorageRepository.getServicePointProviderFormData()
        }
    }

    val countryCode: LiveData<String>
        get() = _countryCode

    val providerCode: LiveData<String>
        get() = _providerCode

    val formFragmentName: LiveData<String>
        get() = _formFragmentName

    val formDataJson: LiveData<String>
        get() = _formDataJson

    fun updateServicePointData(newData: String) {
        _formDataJson.value = newData
        viewModelScope.launch {
            servicePointRepository.setServicePointProviderData(newData)
            settingsStorageRepository.setServicePointProviderFormData(newData)
        }
    }
}
