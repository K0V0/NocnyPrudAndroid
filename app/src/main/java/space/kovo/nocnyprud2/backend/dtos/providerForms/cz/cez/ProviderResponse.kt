package space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProviderResponse (
    val data: ProviderResponseData
)
