package space.kovo.nocnyprud2.ui.activities.wizard

import space.kovo.nocnyprud2.R

class WelcomeActivity : WizardActivityBase<CountrySelectActivity>(
    R.string.welcome_title,
    R.string.welcome_text,
    R.string.welcome_button_next,
    R.layout.wizard_welcome_fragment,
    CountrySelectActivity::class.java
) {

    override fun onStart() {
        super.onStart()
        super.servicePointService.createDefaultServicePointIfNoneExists()
    }
}
