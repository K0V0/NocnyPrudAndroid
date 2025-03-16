package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez

import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez.ProviderQueryForm
import space.kovo.nocnyprud2.backend.enums.HttpMethods
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject
import space.kovo.nocnyprud2.backend.utils.fromJson

//TODO add possible values from configurations like values.yaml
class HttpRequestObjectImpl() : HttpRequestObject {

    override var url: String = "https://dip.cezdistribuce.cz/irj/portal/anonymous/casy-spinani?path=switch-times/signals"

    override var method: String = HttpMethods.POST.name

    override var body: JsonObject = JsonObject()

    override var urlParameters: Map<String, Any> = emptyMap()

    init {
        runBlocking {
            val providerFormJson = ServicePointRepositoryImpl.getInstance().getProviderDataForDefaultServicePoint()
            val providerQueryFormDto = fromJson<ProviderQueryForm>(providerFormJson)
            providerQueryFormDto.eanCode?.let { body.addProperty("ean", it) }
        }
    }

}
