package space.kovo.nocnyprud2.ui.activities.wizard

import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.ui.activities.timetable.TimetableActivity

class ProviderSelectActivity : WizardActivityBase<TimetableActivity> (
    R.string.provider_select_title,
    R.string.provider_select_title,
    R.string.provider_select_button_next,
    R.layout.wizard_provider_select_fragment,
    TimetableActivity::class.java
) {

}
