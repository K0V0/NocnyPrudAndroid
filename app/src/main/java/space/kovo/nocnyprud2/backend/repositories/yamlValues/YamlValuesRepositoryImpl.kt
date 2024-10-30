package space.kovo.nocnyprud2.backend.repositories.yamlValues

import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity
import space.kovo.nocnyprud2.backend.singletons.Values

class YamlValuesRepositoryImpl : YamlValuesRepository {

    override fun getAvailableServiceProviders(countryCode: String): List<ProviderYmlEntity> {
        return Values.wizard.supportedCountries
            .filter { it.id.equals(countryCode) }
            .first()
            .providers
    }
}
