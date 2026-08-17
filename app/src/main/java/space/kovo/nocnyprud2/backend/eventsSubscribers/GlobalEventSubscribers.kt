package space.kovo.nocnyprud2.backend.eventsSubscribers

import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import space.kovo.nocnyprud2.backend.alarms.TariffAlarmScheduler
import space.kovo.nocnyprud2.backend.events.ServicePointEvent
import space.kovo.nocnyprud2.backend.services.TimetableServiceImpl
import space.kovo.nocnyprud2.backend.singletons.AppContext
import space.kovo.nocnyprud2.backend.workers.TimetableRefreshWorker


object GlobalEventSubscribers {

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onServicePointEvent(event: ServicePointEvent) {

        if (event.message == ServicePointEvent.EventType.WIZARD_FLOW_FINISHED) {
            Logger.i("Finished wizard flow.")
            val results = TimetableServiceImpl.getInstance().acquireDataFromProvider()
            TimetableServiceImpl.getInstance().saveAndReplaceTimetable(results)

            // arm the first alarm here rather than leaving it to the timetable screen: that screen
            // opens in parallel with this background work and would otherwise find no rows yet
            AppContext.instance?.let { context ->
                runBlocking {
                    TariffAlarmScheduler.getInstance().rearmFromStoredTimetable(context)
                }
                TimetableRefreshWorker.schedule(context)
            }
        }
    }
}
