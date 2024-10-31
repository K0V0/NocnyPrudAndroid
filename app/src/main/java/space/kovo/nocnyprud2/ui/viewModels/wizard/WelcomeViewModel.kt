package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity

class WelcomeViewModel : WizardViewModelBase() {

    //private val _servicePointEntity = MutableLiveData<ServicePointEntity>()

    init {
        // async
        viewModelScope.launch {
            //TODO nechce logovať
            Logger.d("Current settings stored: " + settingsStorageRepository.getAll().toString())
            //_servicePointEntity.value = servicePointRepository.getOrCreateDefaultServicePoint()
        }
    }
}
