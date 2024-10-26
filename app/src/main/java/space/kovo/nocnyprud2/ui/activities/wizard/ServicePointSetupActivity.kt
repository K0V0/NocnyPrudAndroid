package space.kovo.nocnyprud2.ui.activities.wizard

import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.ui.activities.timetable.TimetableActivity

class ServicePointSetupActivity : WizardActivityBase<TimetableActivity>(
    R.string.service_point_setup_title,
    R.string.service_point_setup_text,
    R.string.service_point_setup_button_next,
    //TODO dynamicky zvolit na zaklade predchadzajucich vyberov
    R.layout.wizard_service_point_setup_fragment_cz_cez,
    TimetableActivity::class.java
) {
}
