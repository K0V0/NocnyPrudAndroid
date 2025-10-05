package space.kovo.nocnyprud2.backend.mappers

interface Mapper<ENTITY_TYPE, DTO_TYPE> {

    fun toEntity(dto: DTO_TYPE): ENTITY_TYPE
    fun toDto(entity: ENTITY_TYPE): DTO_TYPE
}