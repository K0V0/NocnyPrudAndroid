package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.converters.yamlValues.YamlValuesConverter
import space.kovo.nocnyprud2.backend.dtos.yamlValues.SupportedCountryYmlDto

class CountrySelectViewModel : WizardViewModelBase() {

    private val _countryCode = MutableLiveData<String>()
    private val _supportedCountries = MutableLiveData<List<SupportedCountryYmlDto>>()

    init {
        viewModelScope.launch {
            _countryCode.value = settingsStorageRepository.getServicePointCountry()
            _supportedCountries.value = YamlValuesConverter.getSupportedCountriesLocalized(
                yamlValuesRepository.getSupportedCountries())
        }
    }

    val countryCode: LiveData<String>
        get() = _countryCode

    val supportedCountries: LiveData<List<SupportedCountryYmlDto>>
        get() = _supportedCountries

    fun updateCountry(newCountryCode: String) {
        _countryCode.value = newCountryCode
        viewModelScope.launch {
            servicePointRepository.setCountryForDefaultServicePoint(newCountryCode)
            settingsStorageRepository.setServicePointCountry(newCountryCode)
        }
    }
}
