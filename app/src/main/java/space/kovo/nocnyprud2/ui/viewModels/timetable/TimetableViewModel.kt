package space.kovo.nocnyprud2.ui.viewModels.timetable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import space.kovo.nocnyprud2.backend.dtos.timetable.TimetableDto
import space.kovo.nocnyprud2.backend.events.ProviderApiEvent
import space.kovo.nocnyprud2.backend.mappers.timetable.TimetableMapper
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.repositories.database.TimetableRepository
import space.kovo.nocnyprud2.backend.repositories.database.TimetableRepositoryImpl

class TimetableViewModel : ViewModel() {

    companion object {
        val timetableRepository: TimetableRepository = TimetableRepositoryImpl()
        val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()
        val timetableMapper: TimetableMapper = TimetableMapper()
    }

    private val _timetable = MutableLiveData<TimetableDto>()
    val timetable: LiveData<TimetableDto> = _timetable

    init {
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTimetableReady(event: ProviderApiEvent) {
        viewModelScope.launch {
            val pointId = servicePointRepository.getOrCreateDefaultServicePoint()
            val entities = timetableRepository.getTimetables(pointId.uid)
            _timetable.value = timetableMapper.toDto(entities)
        }
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }
}