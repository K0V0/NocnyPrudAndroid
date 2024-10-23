package space.kovo.nocnyprud2.backend.configuration

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

data class Vals(
    val supportedCountries: List<SupportedCountry>,
    val other: String
)

data class SupportedCountry(
    val id: String,
    val name: List<LocalizedName>,
    val providers: List<Provider>
)

data class Provider(
    val id: String,
    val name: List<LocalizedName>
)

data class LocalizedName(
    val lang: String,
    val value: String
)

//TODO singletony proste ani za tú piču nebudú fungovať ako v Spring/SpringBoot
// načítanie YAML je pomalé (aspoň v debugu)
// pozrieť sa na ROOM - in memory databáza alebo nejaké dependency injection frameworky

class Values private constructor(private val context: Context) {

    companion object {

        @Volatile
        private var instance: Values? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: Values(context).also { instance = it }
            }

        fun getValues(): Vals {
            return instance?.passContextAndInitValues() ?: throw Exception("Instance or not initialized")
        }
    }

    private val mapper = ObjectMapper(YAMLFactory()).registerModule(
            KotlinModule.Builder()
//                .withReflectionCacheSize(COMPILED_CODE)
//            .configure(KotlinFeature.NullToEmptyCollection, COMPILED_CODE)
//            .configure(KotlinFeature.NullToEmptyMap, COMPILED_CODE)
//            .configure(KotlinFeature.NullIsSameAsDefault, COMPILED_CODE)
//            .configure(KotlinFeature.SingletonSupport, COMPILED_CODE)
//            .configure(KotlinFeature.StrictNullChecks, COMPILED_CODE)
                .build())

    //private var vals: Vals? = null

//    init {
//        passContextAndInitValues() // Call the initialization method in the init block
//    }

    private fun passContextAndInitValues(): Vals {
//        if (vals != null) {
//            return
//        }
        var reader: BufferedReader? = null
        var lines: String? = null
        try {
            reader = BufferedReader(InputStreamReader(context.assets.open("values.yaml")))
            lines = ""
            reader.forEachLine { l -> lines += String.format("%s:\n", l) }
            return mapper.readValue(lines, Vals::class.java)
        } catch (ioe: IOException) {
            println(ioe.message)
        } finally {
            reader?.close()
        }

        return Vals(emptyList(), "")
    }
}

// system-wide singleton
//class Values(context: Context) {
//
//    init {
//        val mapper = ObjectMapper(YAMLFactory()).registerModule(
//            KotlinModule.Builder()
////                .withReflectionCacheSize(COMPILED_CODE)
////            .configure(KotlinFeature.NullToEmptyCollection, COMPILED_CODE)
////            .configure(KotlinFeature.NullToEmptyMap, COMPILED_CODE)
////            .configure(KotlinFeature.NullIsSameAsDefault, COMPILED_CODE)
////            .configure(KotlinFeature.SingletonSupport, COMPILED_CODE)
////            .configure(KotlinFeature.StrictNullChecks, COMPILED_CODE)
//                .build())
//
//        var vals: Vals? = null
//    }
//
//
//    fun passContextAndInitValues(context: Context) {
//        if (vals != null) {
//            return
//        }
//        var reader: BufferedReader? = null
//        var lines: String? = null
//        try {
//            reader = BufferedReader(InputStreamReader(context.assets.open("values.yaml")))
//            lines = ""
//            reader.forEachLine { l -> lines += String.format("%s:\n", l) }
//            vals = mapper.readValue(lines, Vals::class.java)
//        } catch (ioe: IOException) {
//            println(ioe.message)
//        }
//    }
//
//    fun getAllValues(): Vals {
//        return vals!!
//    }
//}
