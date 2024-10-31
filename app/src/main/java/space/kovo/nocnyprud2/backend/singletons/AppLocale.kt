package space.kovo.nocnyprud2.backend.singletons

import java.util.Locale


object AppLocale {
    val code: String = when (Locale.getDefault().language) {
        "en" -> "en"
        "sk" -> "sk"
        "cz" -> "cz"
        else -> "en"
    }
}
