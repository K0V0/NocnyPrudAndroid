package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez

import java.util.Locale

/**
 *  Translation between the two spellings ČEZ uses for the very same HDO command code.
 *
 *  The EAN lookup on dip.cezdistribuce.cz reports it lower case with the trailing number zero
 *  padded to two digits ("a1b8dp05"), while the public GraphQL API expects it upper case and
 *  unpadded ("A1B8DP5"). Codes with a genuinely two digit tail ("A1B8DP15") do exist, so only
 *  leading zeros may be stripped - never a digit.
 */
class CezHdoCodes {

    companion object {

        private val TRAILING_NUMBER = Regex("^(.*?)(\\d+)$")

        fun normalize(code: String): String {
            val trimmed = code.trim().uppercase(Locale.ROOT)

            // the API also accepts a purely numeric "kód povelu" (217) and a "kód" (AB85);
            // only the povel spelling is zero padded, so anything without letters is left alone
            if (trimmed.none { it.isLetter() }) {
                return trimmed
            }

            val match = TRAILING_NUMBER.matchEntire(trimmed) ?: return trimmed

            val prefix = match.groupValues[1]
            val digits = match.groupValues[2].trimStart('0')

            // a code that is nothing but zeros keeps a single one rather than becoming empty
            return prefix + digits.ifEmpty { "0" }
        }
    }
}
