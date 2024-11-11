package space.kovo.nocnyprud2.backend.entities.yamlValues

data class ProviderYmlEntity (
    val id: String,
    val name: List<LocalizedNameYmlEntity>,
    val servicePointSetupLayout: String
)
