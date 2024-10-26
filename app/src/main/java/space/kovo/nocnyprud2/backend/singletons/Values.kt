package space.kovo.nocnyprud2.backend.singletons

data class Wizard(
    var supportedCountries: List<SupportedCountry>,
    var other: String
)

data class SupportedCountry(
    val id: String,
    val name: List<LocalizedName>,
    val providers: List<Provider>
)

data class Provider(
    val id: String,
    val name: List<LocalizedName>
)

data class LocalizedName(
    val lang: String,
    val value: String
)

data class ValSets(
    var wizard: Wizard
)

object Values {
    // cannot be loaded directly, because for YAML file it needs to wait for application context to be ready
    // fillup with data is performed in space.kovo.nocnyprud2.backend.inits.ValuesInit
    var wizard: Wizard = Wizard(emptyList(), "")
}
