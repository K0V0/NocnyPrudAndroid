package space.kovo.nocnyprud2.backend.entities.yamlValues

data class SupportedCountryEntity(
    val id: String,
    val name: List<LocalizedNameEntity>,
    val providers: List<ProviderEntity>
)

