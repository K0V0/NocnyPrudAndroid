package space.kovo.nocnyprud2.ui.activities.wizard

import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.viewModels
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.ui.viewModels.wizard.SelectCountryViewModel
import space.kovo.nocnyprud2.ui.viewModels.wizard.WelcomeViewModel

class CountrySelectActivity : WizardActivityBase<ProviderSelectActivity>(
    R.string.country_select_title,
    R.string.country_select_text,
    R.string.country_select_button_next,
    R.layout.wizard_country_select_fragment,
    ProviderSelectActivity::class.java
) {
    private val viewModel: SelectCountryViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        this.loadSupportedCountriesToSelectMenu()
    }

    private fun loadSupportedCountriesToSelectMenu() {
        findViewById<ListView>(R.id.wizardCountrySelectListView)
            .setAdapter(CountrySelectSelectAdapter(this) {
                // item click callback
                println("clicked on country $it")
                viewModel.updateCountry(it)
            })
    }
}
