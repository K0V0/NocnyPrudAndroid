package space.kovo.nocnyprud2.backend.repositories.yamlValues

import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity
import space.kovo.nocnyprud2.backend.entities.yamlValues.SupportedCountryYmlEntity
import space.kovo.nocnyprud2.backend.singletons.Values

class YamlValuesRepositoryImpl : YamlValuesRepository {

    override fun getSupportedCountries(): List<SupportedCountryYmlEntity> {
        val result: List<SupportedCountryYmlEntity> = Values.wizard.supportedCountries
        Logger.d("Getting supported countries from YAML repository, values: $result")
        return result
    }

    override fun getAvailableServiceProviders(countryCode: String): List<ProviderYmlEntity> {
        val result: List<ProviderYmlEntity> = Values.wizard.supportedCountries
            .filter { it.id.equals(countryCode) }
            .first()
            .providers
        Logger.d("Getting supported providers from YAML repository, " +
                "countryCode: $countryCode, providers: $result")
        return result
    }
}
