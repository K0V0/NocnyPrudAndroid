package space.kovo.nocnyprud2.backend.services

import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity

interface TimetableService {

    fun acquireDataFromProvider(): List<TimetableEntity>

    fun saveAndReplaceTimetable(data: List<TimetableEntity>)

    /**
     *  Whether the stored timetable is close enough to running out that it should be topped up.
     */
    suspend fun isTimetableRunningOut(): Boolean

    /**
     *  Fetches from the provider, replaces what is stored and re-arms the tariff alarm.
     *  Returns false when the refresh failed and is worth retrying.
     */
    suspend fun refreshTimetable(context: android.content.Context): Boolean
}
