package space.kovo.nocnyprud2.backend.singletons

import com.fasterxml.jackson.databind.ObjectMapper

object YamlMapper {
    var mapper: ObjectMapper = ObjectMapper()
}
