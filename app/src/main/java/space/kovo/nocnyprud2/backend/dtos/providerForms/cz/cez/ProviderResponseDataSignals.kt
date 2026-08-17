package space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ProviderResponseDataSignals(
    val datum: String? = null,
    val casy: String? = null,
    // the HDO command code this service point listens to - the one thing the EAN lookup exists for
    val signal: String? = null
)
