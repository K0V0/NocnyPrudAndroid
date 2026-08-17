package space.kovo.nocnyprud2.backend.services.httpService

import com.google.gson.Gson
import com.orhanobut.logger.Logger
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import space.kovo.nocnyprud2.backend.enums.HttpMethods
import java.io.IOException


class HttpServiceImpl : HttpService {

    companion object {

        val MEDIA_TYPE_JSON: MediaType? = "application/json".toMediaType()
        val ACCEPTABLE_RESPONSE_HTTP_CODES: Array<Int> = arrayOf(200)

        /**
         *  Shared client. The in-memory cookie jar is what makes the ČEZ captcha usable at all:
         *  the captcha image is bound to the session cookie that served it, so the image request
         *  and the lookup that follows have to travel on the same session.
         *  Cookies are deliberately not persisted - a fresh app start gets a fresh session.
         */
        val CLIENT: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(InMemoryCookieJar())
            .build()

        @Volatile
        private var instance: HttpService? = null

        fun getInstance(): HttpService {
            return instance ?: synchronized(this) {
                instance ?: HttpServiceImpl().also { instance = it }
            }
        }
    }

    override fun perform(httpRequestObject: HttpRequestObject): String {
        Logger.d("Performing HTTP request to ${httpRequestObject.toInfo()}")

        val requestBuilder: Request.Builder = Request.Builder()
            .url(httpRequestObject.url)

        httpRequestObject.headers.forEach { (name, value) -> requestBuilder.header(name, value) }

        if (httpRequestObject.method == HttpMethods.GET.name) {

        }
        if (httpRequestObject.method == HttpMethods.POST.name) {
            requestBuilder.post(Gson().toJson(httpRequestObject.body).toRequestBody(MEDIA_TYPE_JSON))
        }

        CLIENT.newCall(requestBuilder.build()).execute().use { response ->
            val body: String = response.body?.string() ?: ""
            if (!ACCEPTABLE_RESPONSE_HTTP_CODES.contains(response.code)) {
                // returning an empty body here would surface much later as an unrelated
                // "no content to map" parsing error, hiding what the provider actually said
                Logger.e("Provider refused the request, HTTP ${response.code}, body: $body")
                throw IOException("Provider responded with HTTP ${response.code}: $body")
            }
            return body
        }
    }
}
