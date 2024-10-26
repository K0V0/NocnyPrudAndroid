package space.kovo.nocnyprud2.backend.inits

import android.content.Context
import space.kovo.nocnyprud2.backend.singletons.Values
import space.kovo.nocnyprud2.backend.singletons.Wizard
import space.kovo.nocnyprud2.backend.singletons.YamlMapper
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class ValuesInit : Init {

    override fun init(context: Context) {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(InputStreamReader(context.assets.open("values.yaml")))
            val lines = reader.readLines().joinToString(separator = "\n")
            Values.wizard = YamlMapper.mapper.readValue(lines, Wizard::class.java)
        } catch (ioe: IOException) {
            //TODO throw exception
            println(ioe.message)
        } finally {
            reader?.close()
        }
    }
}
