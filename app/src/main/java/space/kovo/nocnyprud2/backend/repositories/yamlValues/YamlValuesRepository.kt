package space.kovo.nocnyprud2.backend.repositories.yamlValues

import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity
import space.kovo.nocnyprud2.backend.entities.yamlValues.SupportedCountryYmlEntity

interface YamlValuesRepository {

    fun getSupportedCountries(): List<SupportedCountryYmlEntity>

    fun getAvailableServiceProviders(countryCode: String): List<ProviderYmlEntity>

}
