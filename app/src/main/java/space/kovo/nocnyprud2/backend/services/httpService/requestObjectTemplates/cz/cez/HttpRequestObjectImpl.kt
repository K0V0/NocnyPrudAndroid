package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez

import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez.ProviderForm
import space.kovo.nocnyprud2.backend.enums.HttpMethods
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject
import space.kovo.nocnyprud2.backend.utils.fromJson

//TODO add possible values from configurations like values.yaml
class HttpRequestObjectImpl() : HttpRequestObject {

    override var url: String = ""
        get() = "https://dip.cezdistribuce.cz/irj/portal/anonymous/casy-spinani?path=switch-times/signals"

    override var method: String
        get() = HttpMethods.GET.name
        set(value) {}

    override var body: JsonObject = JsonObject()
        get() {
            // logic of one of possible parameter to identify point of service
            val json = JsonObject()
            runBlocking {
                val providerFormJson = ServicePointRepositoryImpl.getInstance().getProviderDataForDefaultServicePoint()
                val providerFormDto = fromJson<ProviderForm>(providerFormJson)
                providerFormDto.eanCode?.let { json.addProperty("ean", it) }
            }
            return json
        }

    override var urlParameters: Map<String, Any> = emptyMap()
        get() = emptyMap()

}
