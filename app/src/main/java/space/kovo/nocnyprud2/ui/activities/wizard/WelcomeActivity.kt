package space.kovo.nocnyprud2.ui.activities.wizard

import androidx.activity.viewModels
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.ui.viewModels.wizard.WelcomeViewModel

class WelcomeActivity : WizardActivityBase<CountrySelectActivity>(
    R.string.welcome_title,
    R.string.welcome_text,
    R.string.welcome_button_next,
    R.layout.wizard_welcome_fragment,
    CountrySelectActivity::class.java
) {
    private val viewModel: WelcomeViewModel by viewModels()
}
