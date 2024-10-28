package space.kovo.nocnyprud2.ui.viewModels.wizard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.repositories.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.ServicePointRepositoryImpl

class WelcomeViewModel : ViewModel() {

    //TODO dependency injection
    private val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()

    init {
        // async
        viewModelScope.launch {
            servicePointRepository.getOrCreateDefaultServicePoint()
        }
    }
}