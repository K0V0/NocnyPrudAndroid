package space.kovo.nocnyprud2.backend.services.httpService.requestObjectTemplates.cz.cez

import space.kovo.nocnyprud2.backend.dtos.providerForms.cz.cez.ProviderResponse
import space.kovo.nocnyprud2.backend.entities.database.TimetableEntity
import space.kovo.nocnyprud2.backend.services.httpService.HttpResponseHandler
import space.kovo.nocnyprud2.backend.utils.fromJson
import java.text.SimpleDateFormat
import java.util.*

class HttpResponseHandler : HttpResponseHandler {

    companion object {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val timeZone = TimeZone.getTimeZone("Europe/Prague")

        fun convertFullDateStringToEpoch(fullDate: String): Long {
            val calendar = Calendar.getInstance(timeZone)
            calendar.time = dateFormat.parse(fullDate)!!
            return calendar.timeInMillis / 1000
        }
    }

    /**
     *  Example of data returned from API:
     *
     *  {
     *   "data" : {
     *     "signals" : [ {
     *       "signal" : "a1b8dp05",
     *       "den" : "Neděle",
     *       "datum" : "05.10.2025",
     *       "casy" : "00:00-02:55;   11:14-15:15;   22:50-24:00"
     *     },
     *     ...
     *     ],
     *     "amm" : false,
     *     "switchClock" : false,
     *     "unknown" : false,
     *     "partner" : "0022214968",
     *     "vkont" : "000057586133",
     *     "vstelle" : "0001428206",
     *     "anlage" : "0004380772"
     *   },
     *   "statusCode" : 200,
     *   "flashMessages" : [ ]
     * }
     */
    override fun onSuccess(data: String): List<TimetableEntity> {

        val result: MutableList<TimetableEntity> = ArrayList()
        val inputData = fromJson<ProviderResponse>(data)

        val unmergedResults: MutableList<TimetableEntity> = ArrayList()
        inputData.data.signals.forEach { signal ->
            val dateString = signal.datum
            signal.casy.split(";")
                .map { timeRangeString -> timeRangeString.trim() }
                .forEach { timeRangeString ->
                    val timeStrings = timeRangeString.split("-")
                    unmergedResults.add(TimetableEntity(
                        id = 0,
                        servicePointId = 0,
                        sequenceStart = convertFullDateStringToEpoch("$dateString ${timeStrings[0]}"),
                        sequenceEnd = convertFullDateStringToEpoch("$dateString ${timeStrings[1]}")
                    ))
                }
        }

        /**
         *  Merge overlapping timespans.
         *  We assume based on observation that no more than two consecutive timespans could overlap,
         *  so this simple algorithm is good enough
         */
        var i = 0
        while (i < unmergedResults.size - 1) {
            val current = unmergedResults[i]
            val next = unmergedResults[i + 1]
            if (current.sequenceEnd >= next.sequenceStart) {
                result.add(
                    TimetableEntity(
                        id = 0,
                        servicePointId = 0,
                        sequenceStart = current.sequenceStart,
                        sequenceEnd = next.sequenceEnd
                    )
                )
                i += 2
            } else {
                result.add(current)
                i += 1
            }
        }

        if (i == unmergedResults.size - 1) {
            result.add(unmergedResults.last())
        }

        return result
    }
}
