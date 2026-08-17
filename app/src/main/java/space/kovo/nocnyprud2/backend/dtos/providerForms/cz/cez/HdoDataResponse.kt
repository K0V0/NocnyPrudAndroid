package space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 *  Shape of the public ČEZ Distribuce GraphQL answer for the hdoData query.
 *
 *  Note that the meaning of "povel" / "kod_povelu" is NOT stable across regions - in the
 *  "vychod" region the two are swapped - so neither may be used to identify a record.
 *  Which field the query matched on is reported separately in queryDescription.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class HdoDataResponse {
    val data: HdoDataResponseData? = null
    val errors: List<HdoDataResponseError>? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class HdoDataResponseError {
    val message: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class HdoDataResponseData {
    val hdoData: HdoData? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class HdoData {
    val resultPrint: List<HdoDataResultPrint>? = null
    val queryDescription: String? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class HdoDataResultPrint {
    val description: String? = null
    val kod: String? = null
    val kod_povelu: String? = null
    val povel: String? = null
    val rows: List<HdoDataRow>? = null
}

@JsonIgnoreProperties(ignoreUnknown = true)
class HdoDataRow {
    val day: String? = null
    val intervals: List<String>? = null
}
