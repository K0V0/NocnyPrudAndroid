package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.sk.sse

import com.google.gson.JsonObject
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject

data class HttpRequestObjectImpl(
    override var url: String,
    override var method: String,
    override var body: JsonObject,
    override var urlParameters: Map<String, Any>
) : HttpRequestObject
