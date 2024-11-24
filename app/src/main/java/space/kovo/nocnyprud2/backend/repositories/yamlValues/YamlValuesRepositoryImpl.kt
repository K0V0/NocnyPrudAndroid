package space.kovo.nocnyprud2.backend.repositories.yamlValues

import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity
import space.kovo.nocnyprud2.backend.entities.yamlValues.SupportedCountryYmlEntity
import space.kovo.nocnyprud2.backend.singletons.Values

class YamlValuesRepositoryImpl : YamlValuesRepository {

    override fun getSupportedCountries(): List<SupportedCountryYmlEntity> {

        val result: List<SupportedCountryYmlEntity> = Values.wizard.supportedCountries

        Logger.d("Found supported countries from YAML repository, values: $result")

        return result
    }

    override fun getAvailableServiceProviders(countryCode: String): List<ProviderYmlEntity> {

        Logger.d("Getting supported providers from YAML repository for countryCode: $countryCode")

        if (countryCode.isEmpty()) {
            Logger.e("countryCode is empty")
        }

        val result: List<ProviderYmlEntity> = Values.wizard.supportedCountries
            .filter { it.id.equals(countryCode) }
            .first()
            .providers

        Logger.d("Found providers: $result, for countryCode: $countryCode")

        return result
    }

    override fun getServicePointSetupLayout(countryCode: String, providerCode: String): String {

        Logger.d("Getting fragment form template for provider with " +
                "code: $providerCode with countryCode: $countryCode")

        if (countryCode.isEmpty() || providerCode.isEmpty()) {
            Logger.e("countryCode or providerCode is empty, cannot locate fragment template")
        }

        val result: String = Values.wizard.supportedCountries
            .filter { it.id.equals(countryCode) }
            .first()
            .providers
            .filter { it.id.equals(providerCode) }
            .first()
            .servicePointSetupLayout

        Logger.d("Found fragment template to be used: $result")

        return result
    }
}
