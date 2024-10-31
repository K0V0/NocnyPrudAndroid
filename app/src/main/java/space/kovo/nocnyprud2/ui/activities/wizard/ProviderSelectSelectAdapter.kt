package space.kovo.nocnyprud2.ui.activities.wizard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.viewModels
import com.orhanobut.logger.Logger
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.dtos.yamlValues.ProviderYmlDto
import space.kovo.nocnyprud2.ui.viewModels.wizard.ProviderSelectViewModel

class ProviderSelectSelectAdapter(
    val providerSelectActivity: ProviderSelectActivity
) : BaseAdapter() {

    private val viewModel: ProviderSelectViewModel by providerSelectActivity.viewModels()

    private val providerIds = ArrayList<String>()
    private val providerNames = ArrayList<String>()
    private val providerLogoResourceIds = ArrayList<Int>()

    init {
        Logger.d("adapter init")
        viewModel.availableProviders.observe(providerSelectActivity) {
            providers -> this.updateItems(providers)
            super.notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return providerIds.size
    }

    override fun getItem(p0: Int): Any {
        return providerNames.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(i: Int, recycledView: View?, viewGroup: ViewGroup?): View {

        val inflatedListItemView: View = recycledView
            ?: LayoutInflater.from(viewGroup?.context)
                .inflate(R.layout.wizard_provider_select_fragment_list_item, null)

        inflatedListItemView.findViewById<TextView>(R.id.wizardProviderSelectProviderName)
            ?.setText(providerNames.get(i))
        inflatedListItemView.findViewById<TextView>(R.id.wizardProviderSelectProviderId)
            ?.setText(providerIds[i])
        inflatedListItemView.findViewById<CheckBox>(R.id.wizardProviderSelectCheckBox)?.isChecked =
            viewModel.providerCode.value.equals(providerIds[i])

        inflatedListItemView.setOnClickListener {
            viewModel.updateProvider(providerIds[i])
            super.notifyDataSetChanged()
        }

        return inflatedListItemView
    }

    private fun updateItems(availableProviders: List<ProviderYmlDto>) {
        for (availableProvider in availableProviders) {
            providerIds.add(availableProvider.code)
            providerNames.add(availableProvider.name)
        }
    }
}
