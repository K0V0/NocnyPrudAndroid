package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez

import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez.ProviderQueryForm
import space.kovo.nocnyprud2.backend.enums.HttpMethods
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.backend.services.httpService.HttpRequestObject
import space.kovo.nocnyprud2.backend.utils.fromJson

/**
 *  Queries the public ČEZ Distribuce GraphQL API for the switching times of one HDO code.
 *
 *  This is deliberately NOT the dip.cezdistribuce.cz endpoint that resolves an EAN: that one is
 *  guarded by a captcha and would force the user through a challenge on every single refresh.
 *  The timetable is a property of the HDO code (plus region), not of the individual EAN, so once
 *  the code is known - see CezEanCodeLookup, used once during setup - it can be refreshed freely.
 */
class HttpRequestObjectImpl() : HttpRequestObject {

    companion object {
        const val GRAPHQL_URL = "https://www.cezdistribuce.cz/api/graphql"

        const val HDO_QUERY = "query hdoData(\$code: String, \$area: String) {\n" +
                "  hdoData(code: \$code, area: \$area) {\n" +
                "    resultPrint {\n" +
                "      description\n" +
                "      kod\n" +
                "      kod_povelu\n" +
                "      povel\n" +
                "      rows {\n" +
                "        day\n" +
                "        intervals\n" +
                "      }\n" +
                "    }\n" +
                "    queryDescription\n" +
                "  }\n" +
                "}\n"
    }

    override var url: String = GRAPHQL_URL

    override var method: String = HttpMethods.POST.name

    override var body: JsonObject = JsonObject()

    override var urlParameters: Map<String, Any> = emptyMap()

    // the API answers with Czech day names, x-locale just keeps the rest of the payload consistent
    override val headers: Map<String, String> = mapOf("x-locale" to "cs")

    init {
        runBlocking {
            val providerFormJson = ServicePointRepositoryImpl.getInstance()
                .getProviderDataForDefaultServicePoint()
            val form = fromJson<ProviderQueryForm>(providerFormJson)

            val code = form.code?.trim()
            val area = form.area?.trim()

            check(!code.isNullOrEmpty()) { "No HDO code stored for the default service point" }
            check(!area.isNullOrEmpty()) { "No ČEZ region stored for the default service point" }

            val variables = JsonObject().apply {
                addProperty("code", CezHdoCodes.normalize(code))
                addProperty("area", area)
            }

            body.addProperty("operationName", "hdoData")
            body.add("variables", variables)
            body.addProperty("query", HDO_QUERY)
        }
    }
}
