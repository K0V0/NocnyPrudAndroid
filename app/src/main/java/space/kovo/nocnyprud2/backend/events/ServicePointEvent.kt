package space.kovo.nocnyprud2.backend.events

data class ServicePointEvent(val message: EventType) {

    enum class EventType {
        WIZARD_FLOW_FINISHED
    }
}
