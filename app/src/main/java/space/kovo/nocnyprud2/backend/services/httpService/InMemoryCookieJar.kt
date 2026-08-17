package space.kovo.nocnyprud2.backend.services.httpService

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 *  Minimal per-host cookie store kept only for the lifetime of the process.
 *
 *  Providers that guard an endpoint with a captcha tie the challenge to a session cookie, so
 *  without this the captcha can never validate no matter how correctly the user reads the image.
 */
class InMemoryCookieJar : CookieJar {

    private val cookiesPerHost = mutableMapOf<String, MutableMap<String, Cookie>>()

    @Synchronized
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val hostCookies = cookiesPerHost.getOrPut(url.host) { mutableMapOf() }
        cookies.forEach { cookie -> hostCookies[cookie.name] = cookie }
    }

    @Synchronized
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val hostCookies = cookiesPerHost[url.host] ?: return emptyList()
        val now = System.currentTimeMillis()

        // drop what expired instead of replaying stale cookies back to the provider
        hostCookies.entries.removeAll { (_, cookie) -> cookie.expiresAt < now }

        return hostCookies.values.filter { cookie -> cookie.matches(url) }
    }
}
