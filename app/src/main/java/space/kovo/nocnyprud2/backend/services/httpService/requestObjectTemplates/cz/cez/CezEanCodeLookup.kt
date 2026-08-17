package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.JsonObject
import com.orhanobut.logger.Logger
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez.ProviderResponse
import space.kovo.nocnyprud2.backend.services.httpService.HttpServiceImpl
import space.kovo.nocnyprud2.backend.utils.fromJson
import java.io.IOException

/**
 *  One-off resolution of an EAN into the HDO command code that drives its tariff switching.
 *
 *  This is the only place in the app that touches the captcha guarded portal, and it is meant to
 *  run once during setup: afterwards the code alone is enough to refresh the timetable from the
 *  public API for good. Users who can read the code off their HDO receiver never come here.
 *
 *  Both calls go through the shared HttpServiceImpl.CLIENT on purpose - the captcha is bound to
 *  the session cookie that delivered the image, so image and lookup must share a session.
 */
class CezEanCodeLookup {

    companion object {

        private const val PORTAL_URL =
            "https://dip.cezdistribuce.cz/irj/portal/anonymous/casy-spinani"

        private const val SIGNALS_URL =
            "$PORTAL_URL?path=switch-times/signals"

        private const val CAPTCHA_URL =
            "https://dip.cezdistribuce.cz/irj/portal/anonymous/captcha"

        /** ČEZ error key reported when the rewritten captcha text does not match */
        private const val CAPTCHA_ERROR_KEY = "CPT-002"

        /**
         *  The portal behind these endpoints sniffs the user agent and serves
         *  "Could not open iView. The iView is not compatible with your browser" - as an HTML page
         *  under a 200 - to anything it does not recognise as a browser. This is an honest
         *  description of the device the request really comes from, it just has to be spelled in
         *  a shape the portal accepts.
         */
        private const val USER_AGENT = "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/126.0.0.0 Mobile Safari/537.36"

        private val MEDIA_TYPE_JSON = "application/json".toMediaType()

        fun getInstance(): CezEanCodeLookup = CezEanCodeLookup()
    }

    /**
     *  Loads a fresh captcha image, establishing the session it belongs to.
     */
    fun fetchCaptcha(): Bitmap {
        // hitting the portal page first is what hands out the session cookie
        HttpServiceImpl.CLIENT.newCall(
            Request.Builder().url(PORTAL_URL).header("User-Agent", USER_AGENT).build()
        ).execute().use { it.body?.string() }

        val request = Request.Builder()
            .url("$CAPTCHA_URL?t=${System.currentTimeMillis()}")
            .header("User-Agent", USER_AGENT)
            .build()

        HttpServiceImpl.CLIENT.newCall(request).execute().use { response ->
            val bytes = response.body?.bytes()

            if (!response.isSuccessful || bytes == null || bytes.isEmpty()) {
                throw IOException("Could not load captcha, HTTP ${response.code}")
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                ?: throw IOException(
                    "Captcha image could not be decoded, HTTP ${response.code}, " +
                            "content-type ${response.header("Content-Type")}, " +
                            "${bytes.size} bytes, starts with " +
                            String(bytes.copyOfRange(0, minOf(120, bytes.size))))
        }
    }

    /**
     *  Resolves the EAN into a normalized HDO code, or throws [CaptchaRejectedException] when the
     *  rewritten text was wrong so that the caller can just offer a new image.
     */
    fun resolveCode(ean: String, captcha: String): String {

        val body = JsonObject().apply {
            addProperty("ean", ean.trim())
            addProperty("captcha", captcha.trim())
        }

        val request = Request.Builder()
            .url(SIGNALS_URL)
            .header("X-Skip-Unwrap", "true")
            .header("User-Agent", USER_AGENT)
            .post(body.toString().toRequestBody(MEDIA_TYPE_JSON))
            .build()

        HttpServiceImpl.CLIENT.newCall(request).execute().use { response ->
            val payload = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                Logger.e("EAN lookup refused, HTTP ${response.code}, body: $payload")

                if (payload.contains(CAPTCHA_ERROR_KEY)) {
                    throw CaptchaRejectedException()
                }
                throw IOException("EAN lookup failed with HTTP ${response.code}")
            }

            val signal = fromJson<ProviderResponse>(payload)
                .data.signals
                .firstNotNullOfOrNull { it.signal?.takeIf(String::isNotBlank) }
                ?: throw NoCodeForEanException()

            Logger.i("Resolved EAN to HDO signal $signal")

            return CezHdoCodes.normalize(signal)
        }
    }
}

/** the text rewritten from the captcha image did not match */
class CaptchaRejectedException : IOException("Captcha was rejected")

/** the EAN is valid as far as the portal is concerned, but carries no HDO signal */
class NoCodeForEanException : IOException("No HDO code is available for this EAN")
