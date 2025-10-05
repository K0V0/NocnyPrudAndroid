package space.kovo.nocnyprud2.backend.events

class ProviderApiEvent(val message: EventType) {

    enum class EventType {
        TIMESPANS_QUERIED_PARSED_AND_SAVED
    }
}