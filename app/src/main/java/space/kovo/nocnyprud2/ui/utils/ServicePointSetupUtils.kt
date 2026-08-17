package space.kovo.nocnyprud2.ui.utils

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class ServicePointSetupFormsPopulator {

    companion object {
        private val FORMS_DATA_TYPE = object : TypeToken<Map<String, String>>() {}.type

        fun extractDataFromForm(formViewGroup: ViewGroup): String {
            val tempMap = mutableMapOf<String, String>()

            forEachNamedField(formViewGroup) { key, child ->
                readFieldValue(child)?.let { value -> putDataIntoMap(tempMap, key, value) }
            }

            return JSONObject(tempMap as Map<*, *>).toString()
        }

        fun deserializeJsonAndPopulateFields(formViewGroup: ViewGroup, jsonString: String) {
            val gson = Gson()
            val deserializedMap: Map<String, String> =
                gson.fromJson(jsonString, FORMS_DATA_TYPE) ?: return

            if (deserializedMap.isEmpty()) {
                return
            }

            forEachNamedField(formViewGroup) { key, child ->
                deserializedMap[key]?.let { value -> writeFieldValue(child, value) }
            }
        }

        /**
         *  Walks the whole form tree, not only the direct children, so that provider forms are
         *  free to group their fields into nested layouts.
         */
        private fun forEachNamedField(viewGroup: ViewGroup, action: (String, View) -> Unit) {
            for (i in 0 until viewGroup.childCount) {
                val child = viewGroup.getChildAt(i)

                // fields have to be recognised before recursing: a Spinner is itself a ViewGroup,
                // so descending into it blindly would walk past the very value we want to read
                if (isFormField(child)) {
                    if (child.id != View.NO_ID) {
                        action(child.resources.getResourceEntryName(child.id), child)
                    }
                    continue
                }
                if (child is ViewGroup) {
                    forEachNamedField(child, action)
                }
            }
        }

        private fun isFormField(view: View): Boolean = view is EditText || view is Spinner

        private fun readFieldValue(view: View): String? = when (view) {
            is EditText -> view.text.toString()
            // adapter is expected to expose the stored key, see KeyLabelSpinnerAdapter
            is Spinner -> view.selectedItem?.toString()
            else -> null
        }

        private fun writeFieldValue(view: View, value: String) {
            when (view) {
                is EditText -> view.setText(value)
                is Spinner -> {
                    val position = (view.adapter as? KeyLabelSpinnerAdapter)?.positionOfKey(value)
                    if (position != null && position >= 0) {
                        view.setSelection(position)
                    }
                }
            }
        }

        private fun putDataIntoMap(tempMap: MutableMap<String, String>, key: String, value: String) {
            tempMap[key] = value
        }
    }
}
