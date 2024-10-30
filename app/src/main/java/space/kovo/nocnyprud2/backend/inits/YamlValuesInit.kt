package space.kovo.nocnyprud2.backend.inits

import android.content.Context
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.entities.yamlValues.WizardYmlEntity
import space.kovo.nocnyprud2.backend.singletons.Values
import space.kovo.nocnyprud2.backend.singletons.YamlMapper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class YamlValuesInit : Init {

    override fun init(context: Context) {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(context.resources.openRawResource(R.raw.values)))
            val lines = reader.readLines().joinToString(separator = "\n")
            Values.wizard = YamlMapper.mapper.readValue(lines, WizardYmlEntity::class.java)
        } catch (ioe: IOException) {
            //TODO throw exception
            println(ioe.message)
        } finally {
            reader?.close()
        }
    }
}
