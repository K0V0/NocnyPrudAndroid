package space.kovo.nocnyprud2.ui.activities.wizard

import android.widget.ListView
import space.kovo.nocnyprud2.R

class ProviderSelectActivity : WizardActivityBase<ServicePointSetupActivity> (
    R.string.provider_select_title,
    R.string.provider_select_title,
    R.string.provider_select_button_next,
    R.layout.wizard_provider_select_fragment,
    ServicePointSetupActivity::class.java
) {
    override fun onStart() {
        super.onStart()
        this.loadAvailableProvidersToSelectMenu()
    }

    private fun loadAvailableProvidersToSelectMenu() {
        findViewById<ListView>(R.id.wizardProviderSelectListView)
            .setAdapter(ProviderSelectSelectAdapter(this))
    }
}
