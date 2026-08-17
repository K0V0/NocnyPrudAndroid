package space.kovo.nocnyprud2.backend.services.httpService

import com.google.gson.JsonObject
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity

interface HttpRequestObject {
    var url: String
    var method: String
    var body: JsonObject
    var urlParameters: Map<String, Any>

    /**
     *  Extra request headers. Some provider APIs need them to answer at all - ČEZ for instance
     *  keys the language of the returned day names off "x-locale".
     */
    val headers: Map<String, String>
        get() = emptyMap()

    fun toInfo(): String {
        return "Request -> URL: $url, UrlParameters: $urlParameters, Method: $method, " +
                "Headers: ${headers.keys}, Body: $body"
    }
}

interface HttpResponseHandler {
    fun onSuccess(data: String): List<TimetableEntity>
}

interface HttpService {

    fun perform(httpRequestObject: HttpRequestObject): String
}
