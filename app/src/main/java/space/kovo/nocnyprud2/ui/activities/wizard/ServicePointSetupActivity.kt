package space.kovo.nocnyprud2.ui.activities.wizard

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.viewModels
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.events.ServicePointEvent
import space.kovo.nocnyprud2.ui.activities.timetable.TimetableActivity
import space.kovo.nocnyprud2.ui.utils.ServicePointSetupFormsPopulator
import space.kovo.nocnyprud2.ui.viewModels.wizard.ServicePointSetupViewModel

class ServicePointSetupActivity : WizardActivityBase<TimetableActivity>(
    R.string.service_point_setup_title,
    R.string.service_point_setup_text,
    R.string.service_point_setup_button_next,
    R.layout.wizard_service_point_setup_fragment_dummy,
    TimetableActivity::class.java
) {
    private val viewModel: ServicePointSetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.insertForm()
        this.fillDataIntoForm()
    }

    override fun onNextClick() {
        super.onNextClick()
        this.populateAndSaveFormData()
    }

    private fun insertForm() {
        viewModel.formFragmentName.observe(this) {
            // switch to fragment with form(s) for given energy provider to be inserted into frameLayout
            fragmentTemplateName -> this.setupFrame(resources.getIdentifier(fragmentTemplateName, "layout", packageName))
        }
    }

    private fun fillDataIntoForm() {
        // prefill form with lastly known values if any of them available (unfinished / re-run flow)
        viewModel.formDataJson.observe(this) {
            formDataJson -> ServicePointSetupFormsPopulator
                .deserializeJsonAndPopulateFields(super.fragment?.view as ViewGroup, formDataJson)
        }
    }

    private fun populateAndSaveFormData() {
        val data: String = ServicePointSetupFormsPopulator.extractDataFromForm(super.fragment?.view as ViewGroup)
        Logger.i("Obtained end jsoned data for service point setup {}", data)
        viewModel.updateServicePointData(data) {
            // send event after data have been serialized and saved
            EventBus.getDefault().post(ServicePointEvent(ServicePointEvent.EventType.WIZARD_FLOW_FINISHED))
        }
    }
}
