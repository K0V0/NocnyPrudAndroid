package space.kovo.nocnyprud2.backend.mappers.timetable

import space.kovo.nocnyprud2.backend.dtos.timetable.TimetableDto
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity
import space.kovo.nocnyprud2.backend.mappers.Mapper

class TimetableMapper : Mapper<List<TimetableEntity>, TimetableDto> {

    override fun toEntity(dto: TimetableDto): List<TimetableEntity> {
        TODO("Not yet implemented")
    }

    override fun toDto(entity: List<TimetableEntity>): TimetableDto {
        TODO("Not yet implemented")
    }
}