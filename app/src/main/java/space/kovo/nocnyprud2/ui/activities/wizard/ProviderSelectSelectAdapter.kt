package space.kovo.nocnyprud2.ui.activities.wizard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.activity.viewModels
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderYmlEntity
import space.kovo.nocnyprud2.ui.viewModels.wizard.ProviderSelectViewModel

class ProviderSelectSelectAdapter(providerSelectActivity: ProviderSelectActivity) : BaseAdapter() {

    private val viewModel: ProviderSelectViewModel by  providerSelectActivity.viewModels()

    private val providerIds = ArrayList<String>()
    private val providerNames = ArrayList<String>()
    private val providerLogoResourceIds = ArrayList<Int>()

    init {
        viewModel.availableProviders.observe(providerSelectActivity) {
                providers -> this.updateItems(providers)
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

        inflatedListItemView.findViewById<TextView>(R.id.wizardProviderSelectCountryName)
            ?.setText(providerNames.get(i))
        inflatedListItemView.findViewById<TextView>(R.id.wizardProviderSelectCountryId)
            ?.setText(providerIds[i])

        return inflatedListItemView
    }

    private fun updateItems(availableProviders: List<ProviderYmlEntity>) {
        for (provider in availableProviders) {
            providerIds.add(provider.id)
            //TODO get system language
            providerNames.add(
                provider.name
                    .first { country -> country.lang.equals("cz") }.value
            )
        }
        super.notifyDataSetChanged()
    }
}
