package space.kovo.nocnyprud2.backend.services

import space.kovo.nocnyprud2.backend.entities.database.ServicePointEntity

interface TimetableService {

    fun acquireDataFromProvider(): List<ServicePointEntity>
}
