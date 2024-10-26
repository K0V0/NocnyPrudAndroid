package space.kovo.nocnyprud2.ui.activities.wizard

import android.widget.ListView
import space.kovo.nocnyprud2.R

class CountrySelectActivity : WizardActivityBase<ProviderSelectActivity>(
    R.string.country_select_title,
    R.string.country_select_text,
    R.string.country_select_button_next,
    R.layout.wizard_country_select_fragment,
    ProviderSelectActivity::class.java
) {

    override fun onStart() {
        super.onStart()
        this.loadSupportedCountriesToSelectMenu()
    }

    private fun loadSupportedCountriesToSelectMenu() {
        findViewById<ListView>(R.id.wizardCountrySelectListView)
            .setAdapter(CountrySelectSelectAdapter(this))
    }
}
