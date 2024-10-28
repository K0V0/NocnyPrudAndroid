package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepository
import space.kovo.nocnyprud2.backend.repositories.settingsStorage.SettingsStorageRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl

class CountrySelectViewModel : ViewModel() {

    companion object {
        //TODO dependency injection somehow
        private val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()
        private val settingsStorageRepository: SettingsStorageRepository = SettingsStorageRepositoryImpl()
    }

    private val _countryCode = MutableLiveData<String>()

    val countryCode: LiveData<String>
        get() = _countryCode

    fun updateCountry(newCountryCode: String) {
        _countryCode.value = newCountryCode
        viewModelScope.launch {
            servicePointRepository.setCountryForDefaultServicePoint(newCountryCode)
            settingsStorageRepository.setServicePointCountry(newCountryCode)
        }
    }
}
