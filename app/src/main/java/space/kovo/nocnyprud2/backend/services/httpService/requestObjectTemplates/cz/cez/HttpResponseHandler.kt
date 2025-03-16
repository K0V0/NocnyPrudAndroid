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

    override fun onSuccess(data: String): List<TimetableEntity> {

        val result: MutableList<TimetableEntity> = ArrayList()
        val inputData = fromJson<ProviderResponse>(data)

        inputData.data.signals.forEach { signal ->
            val dateString = signal.datum
            signal.casy.split(";")
                .map { timeRangeString -> timeRangeString.trim() }
                .forEach { timeRangeString ->
                    val timeStrings = timeRangeString.split("-")
                    result.add(TimetableEntity(
                        id = 0,
                        servicePointId = 0,
                        sequenceStart = convertFullDateStringToEpoch("$dateString ${timeStrings[0]}"),
                        sequenceEnd = convertFullDateStringToEpoch("$dateString ${timeStrings[1]}")
                    ))
                }
        }

        return result
    }
}
