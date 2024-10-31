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
import space.kovo.nocnyprud2.backend.dtos.yamlValues.SupportedCountryYmlDto
import space.kovo.nocnyprud2.ui.viewModels.wizard.CountrySelectViewModel

class CountrySelectSelectAdapter(
    val countrySelectActivity: CountrySelectActivity
) : BaseAdapter() {

    private val viewModel: CountrySelectViewModel by countrySelectActivity.viewModels()

    private val countryCodes = ArrayList<String>()
    private val countryNames = ArrayList<String>()
    private val flagResourceIds = ArrayList<Int>()

    init {
        Logger.d("adapter init")
        viewModel.supportedCountries.observe(countrySelectActivity) {
            countries -> this.updateItems(countries)
            super.notifyDataSetChanged()
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
            ?: LayoutInflater.from(viewGroup?.context)
                .inflate(R.layout.wizard_country_select_fragment_list_item, null)

        inflatedListItemView.findViewById<TextView>(R.id.wizardCountrySelectCountryName)
            ?.setText(countryNames.get(i))
        inflatedListItemView.findViewById<TextView>(R.id.wizardCountrySelectCountryId)
            ?.setText(countryCodes[i])
        inflatedListItemView.findViewById<CheckBox>(R.id.wizardCountrySelectCheckBox).isChecked =
            viewModel.countryCode.value.equals(countryCodes[i])

        inflatedListItemView.setOnClickListener {
            viewModel.updateCountry(countryCodes[i])
            super.notifyDataSetChanged()
        }

        return inflatedListItemView
    }

    fun updateItems(supportedCountries: List<SupportedCountryYmlDto>) {
        for (supportedCountry in supportedCountries) {
            countryCodes.add(supportedCountry.code)
            countryNames.add(supportedCountry.name)
        }
    }
}
