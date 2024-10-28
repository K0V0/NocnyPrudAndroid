package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.repositories.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.ServicePointRepositoryImpl

class SelectCountryViewModel : ViewModel() {

    private val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()

    private val _countryCode = MutableLiveData<String>()

    val countryCode: LiveData<String>
        get() = _countryCode

    fun updateCountry(newCountryCode: String) {
        _countryCode.value = newCountryCode
        viewModelScope.launch {
            servicePointRepository.setCountryForDefaultServicePoint(newCountryCode)
        }
    }
}
