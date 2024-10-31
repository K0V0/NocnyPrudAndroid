package space.kovo.nocnyprud2.backend.converters.yamlValues

import space.kovo.nocnyprud2.backend.dtos.yamlValues.ProviderYmlDto
import space.kovo.nocnyprud2.backend.dtos.yamlValues.SupportedCountryYmlDto
import space.kovo.nocnyprud2.backend.entities.yamlValues.LocalizedNameYmlEntity
import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity
import space.kovo.nocnyprud2.backend.entities.yamlValues.SupportedCountryYmlEntity
import space.kovo.nocnyprud2.backend.singletons.AppLocale

class YamlValuesConverter {

    companion object {

        fun getSupportedCountriesLocalized(entities: List<SupportedCountryYmlEntity>): List<SupportedCountryYmlDto> {
            return entities
                .map { entity -> SupportedCountryYmlDto(entity.id, getI18nizedName(entity.name)) }
        }

        fun getProvidersLocalized(entities: List<ProviderYmlEntity>) : List<ProviderYmlDto> {
            return entities
                .map { entity -> ProviderYmlDto(entity.id, getI18nizedName(entity.name)) }
        }

        private fun getI18nizedName(localizedNameYmlEntity: List<LocalizedNameYmlEntity>): String {
            return localizedNameYmlEntity
                .first { entity -> entity.lang.equals(AppLocale.code) }
                .value
        }
    }
}
