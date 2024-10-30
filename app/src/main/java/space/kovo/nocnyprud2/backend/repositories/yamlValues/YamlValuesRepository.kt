package space.kovo.nocnyprud2.backend.repositories.yamlValues

import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity

interface YamlValuesRepository {
    fun getAvailableServiceProviders(countryCode: String): List<ProviderYmlEntity>
}
