package space.kovo.nocnyprud2.ui.activities.wizard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.entities.yamlValues.ProviderEntity
import space.kovo.nocnyprud2.backend.singletons.Values

class ProviderSelectSelectAdapter(applicationContext: Context) : BaseAdapter() {

    private val inflater: LayoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val providerIds = ArrayList<String>()
    private val providerNames = ArrayList<String>()
    private val providerLogoResourceIds = ArrayList<Int>()

    init {
        //TODO na vsetky taketo operacie s datami z YAML napisat nejaku util classu
        val availableProviders: List<ProviderEntity> = Values.wizard.supportedCountries.get(0).providers;
        for (provider in availableProviders) {
            providerIds.add(provider.id)
            //TODO get system language
            providerNames.add(
                provider.name
                    .first { country -> country.lang.equals("cz") }.value
            )
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
            ?: inflater.inflate(R.layout.wizard_provider_select_fragment_list_item, null)

        inflatedListItemView.findViewById<TextView>(R.id.wizardProviderSelectCountryName)
            ?.setText(providerNames.get(i))
        inflatedListItemView.findViewById<TextView>(R.id.wizardProviderSelectCountryId)
            ?.setText(providerIds[i])

        return inflatedListItemView
    }
}
