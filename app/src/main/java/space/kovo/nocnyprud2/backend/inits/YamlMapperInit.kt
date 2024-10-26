package space.kovo.nocnyprud2.backend.inits

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import space.kovo.nocnyprud2.backend.singletons.YamlMapper

class YamlMapperInit : Init {

    override fun init(context: Context) {
        YamlMapper.mapper = ObjectMapper(YAMLFactory()).registerModule(
            KotlinModule.Builder()
//                .withReflectionCacheSize(COMPILED_CODE)
//            .configure(KotlinFeature.NullToEmptyCollection, COMPILED_CODE)
//            .configure(KotlinFeature.NullToEmptyMap, COMPILED_CODE)
//            .configure(KotlinFeature.NullIsSameAsDefault, COMPILED_CODE)
//            .configure(KotlinFeature.SingletonSupport, COMPILED_CODE)
//            .configure(KotlinFeature.StrictNullChecks, COMPILED_CODE)
                .build())
    }
}
