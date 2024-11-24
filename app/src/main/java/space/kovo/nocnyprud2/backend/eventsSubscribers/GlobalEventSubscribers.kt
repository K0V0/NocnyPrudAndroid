package space.kovo.nocnyprud2.backend.eventsSubscribers

import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import space.kovo.nocnyprud2.backend.events.ServicePointEvent


object GlobalEventSubscribers {

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onServicePointEvent(event: ServicePointEvent) {

        if (event.message == ServicePointEvent.EventType.WIZARD_FLOW_FINISHED) {
            Logger.i("Finished wizard flow.")
        }
    }
}
