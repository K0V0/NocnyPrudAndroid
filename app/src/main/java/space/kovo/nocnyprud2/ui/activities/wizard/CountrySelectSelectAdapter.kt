package space.kovo.nocnyprud2.ui.activities.wizard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.singletons.SupportedCountry
import space.kovo.nocnyprud2.backend.singletons.Values

class CountrySelectSelectAdapter(
    val applicationContext: Context//,
    //val clickListener: AdapterView.OnItemClickListener
    , val fx: (countryCode: String) -> Unit
) : BaseAdapter() {

    private val inflater: LayoutInflater = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private val countryCodes = ArrayList<String>()
    private val countryNames = ArrayList<String>()
    private val flagResourceIds = ArrayList<Int>()

    init {
        val supportedCountries: List<SupportedCountry> = Values.wizard.supportedCountries;
        for (supportedCountry in supportedCountries) {
            countryCodes.add(supportedCountry.id)
            //TODO get system language
            countryNames.add(
                supportedCountry.name
                    .first { country -> country.lang.equals("cz") }.value
            )
        }
    }

    override fun getCount(): Int {
        return countryCodes.size
    }

    override fun getItem(p0: Int): Any {
        return countryNames.get(p0);
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(i: Int, recycledView: View?, viewGroup: ViewGroup?): View {
        val inflatedListItemView: View = recycledView
            ?: inflater.inflate(R.layout.wizard_country_select_fragment_list_item, null)

        inflatedListItemView.findViewById<TextView>(R.id.wizardCountrySelectCountryName)
            ?.setText(countryNames.get(i))
        inflatedListItemView.findViewById<TextView>(R.id.wizardCountrySelectCountryId)
            ?.setText(countryCodes[i])

        inflatedListItemView.setOnClickListener {
            println("inside adapter")
            fx(countryCodes[i])
        }

        return inflatedListItemView
    }
}
