package space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 *  The setup form data as stored for the ČEZ provider.
 *
 *  Property names mirror the view ids of wizard_service_point_setup_fragment_cz_cez, because
 *  ServicePointSetupFormsPopulator keys the stored JSON on the resource entry name of each field.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class ProviderQueryForm {

    @JsonProperty("service_point_setup_cz_cez_code")
    val code: String? = null

    @JsonProperty("service_point_setup_cz_cez_area")
    val area: String? = null
}
