package space.kovo.nocnyprud2.backend.repositories.yamlValues

import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderEntity

interface YamlValuesRepository {
    fun getAvailableServiceProviders(countryCode: String): List<ProviderEntity>
}
