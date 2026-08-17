package space.kovo.nocnyprud2.ui.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 *  Spinner adapter over two index-aligned arrays: the machine readable keys that get stored and
 *  sent to the provider, and the labels the user actually sees.
 *
 *  getItem() intentionally returns the KEY, so that whatever reads the spinner back
 *  (ServicePointSetupFormsPopulator) stores a stable value that never changes with wording
 *  or translation of the labels.
 */
class KeyLabelSpinnerAdapter(
    context: Context,
    private val keys: List<String>,
    private val labels: List<String>
) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, keys) {

    companion object {

        fun fromArrayResources(
            context: Context,
            keysArrayResId: Int,
            labelsArrayResId: Int
        ): KeyLabelSpinnerAdapter {
            val keys = context.resources.getStringArray(keysArrayResId).toList()
            val labels = context.resources.getStringArray(labelsArrayResId).toList()
            require(keys.size == labels.size) {
                "Spinner key/label arrays must be index-aligned, got ${keys.size} keys and ${labels.size} labels"
            }
            return KeyLabelSpinnerAdapter(context, keys, labels)
        }
    }

    init {
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    fun positionOfKey(key: String?): Int = keys.indexOf(key)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent).also { applyLabel(it, position) }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent).also { applyLabel(it, position) }
    }

    private fun applyLabel(view: View, position: Int) {
        (view as? TextView)?.text = labels.getOrElse(position) { keys.getOrElse(position) { "" } }
    }
}
