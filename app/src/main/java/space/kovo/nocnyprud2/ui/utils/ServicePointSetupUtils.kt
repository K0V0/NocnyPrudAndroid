package space.kovo.nocnyprud2.ui.utils

import android.view.ViewGroup
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class ServicePointSetupFormsPopulator {

    companion object {
        private val FORMS_DATA_TYPE = object : TypeToken<Map<String, String>>() {}.type

        fun extractDataFromForm(formViewGroup: ViewGroup): String {
            val formData: Map<String, String>
            val tempMap = mutableMapOf<String, String>()

            for (i in 0 until formViewGroup.childCount) {
                val child = formViewGroup.getChildAt(i)
                val key = child.resources.getResourceEntryName(child.id)

                if (child is EditText) {
                    putDataIntoMap(tempMap, key, child.text.toString())
                }
            }
            formData = tempMap
            return JSONObject(formData).toString();
        }

        fun deserializeJsonAndPopulateFields(formViewGroup: ViewGroup, jsonString: String) {
            val gson = Gson()
            val deserializedMap: Map<String, String> = gson.fromJson(jsonString, FORMS_DATA_TYPE)

            for (i in 0 until formViewGroup.childCount) {
                val child = formViewGroup.getChildAt(i)
                val key = child.resources.getResourceEntryName(child.id)

                if (child is EditText) {
                    deserializedMap[key]?.let { value -> child.setText(value) }
                }
            }
        }

        private fun putDataIntoMap(tempMap: MutableMap<String, String>, key: String, value: String) {
            tempMap[key] = value
        }
    }
}
