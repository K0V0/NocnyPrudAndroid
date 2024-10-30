package space.kovo.nocnyprud2.backend.entities.yamlValues

data class SupportedCountryYmlEntity(
    val id: String,
    val name: List<LocalizedNameYmlEntity>,
    val providers: List<ProviderYmlEntity>
)

