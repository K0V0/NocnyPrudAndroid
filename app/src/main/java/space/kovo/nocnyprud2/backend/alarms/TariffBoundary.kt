package space.kovo.nocnyprud2.backend.alarms

import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity

/**
 *  A moment at which the tariff flips, derived from the stored timetable.
 *
 *  Sequences in the database are the low tariff windows, so every sequence contributes two
 *  boundaries: the low tariff starting and it ending again.
 */
data class TariffBoundary(
    val epochSeconds: Long,
    val type: Type
) {
    enum class Type { LOW_TARIFF_STARTS, LOW_TARIFF_ENDS }

    companion object {

        fun fromTimetable(entities: List<TimetableEntity>): List<TariffBoundary> {
            return entities
                .flatMap { entity ->
                    listOf(
                        TariffBoundary(entity.sequenceStart, Type.LOW_TARIFF_STARTS),
                        TariffBoundary(entity.sequenceEnd, Type.LOW_TARIFF_ENDS)
                    )
                }
                .sortedBy { it.epochSeconds }
        }
    }
}
