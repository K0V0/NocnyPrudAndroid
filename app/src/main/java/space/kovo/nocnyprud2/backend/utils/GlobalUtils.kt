package space.kovo.nocnyprud2.backend.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

inline fun <reified T> fromJson(json: String): T {
    return jacksonObjectMapper().readValue(json, T::class.java)
}
