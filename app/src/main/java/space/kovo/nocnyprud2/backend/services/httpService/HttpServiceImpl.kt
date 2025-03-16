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


class HttpServiceImpl : HttpService {

    companion object {

        val MEDIA_TYPE_JSON: MediaType? = "application/json".toMediaType()
        val ACCEPTABLE_RESPONSE_HTTP_CODES: Array<Int> = arrayOf(200)

        val CLIENT: OkHttpClient = OkHttpClient()

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

        if (httpRequestObject.method == HttpMethods.GET.name) {

        }
        if (httpRequestObject.method == HttpMethods.POST.name) {
            requestBuilder.post(Gson().toJson(httpRequestObject.body).toRequestBody(MEDIA_TYPE_JSON))
        }

        CLIENT.newCall(requestBuilder.build()).execute().use { response ->
            if (ACCEPTABLE_RESPONSE_HTTP_CODES.contains(response.code)) {
                return response.body?.string() ?: ""
            }
        }

        return ""
    }
}
