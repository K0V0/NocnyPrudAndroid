package space.kovo.nocnyprud2.ui.activities.wizard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.orhanobut.logger.Logger
import org.greenrobot.eventbus.EventBus
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.events.ServicePointEvent
import space.kovo.nocnyprud2.ui.activities.timetable.TimetableActivity
import space.kovo.nocnyprud2.ui.utils.KeyLabelSpinnerAdapter
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

    /**
     *  Registered eagerly, as activity result contracts may not be registered once the activity
     *  is already started.
     */
    private val eanLookup = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringExtra(CezEanLookupActivity.RESULT_HDO_CODE)
                ?.let { code -> this.applyResolvedCode(code) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.insertForm()
        this.fillDataIntoForm()
    }

    override fun onNextClick(): Boolean {
        if (!this.isFormFilledIn()) {
            Toast.makeText(this, R.string.service_point_setup_error_missing_code, Toast.LENGTH_LONG)
                .show()
            return false
        }
        this.populateAndSaveFormData()
        return true
    }

    /**
     *  Wires up the parts of a provider form that are more than a plain text field. Runs on every
     *  (re)insertion of the form, so everything here has to be safe to apply repeatedly.
     */
    override fun onFormInserted() {
        val form = super.fragment?.view as? ViewGroup ?: return

        form.findViewById<Spinner>(R.id.service_point_setup_cz_cez_area)?.let { spinner ->
            spinner.adapter = KeyLabelSpinnerAdapter.fromArrayResources(
                this, R.array.cz_cez_area_keys, R.array.cz_cez_area_labels)
        }

        form.findViewById<Button>(R.id.service_point_setup_cz_cez_ean_lookup_button)
            ?.setOnClickListener {
                eanLookup.launch(Intent(this, CezEanLookupActivity::class.java))
            }
    }

    private fun insertForm() {
        viewModel.formFragmentName.observe(this) {
            // switch to fragment with form(s) for given energy provider to be inserted into frameLayout
            fragmentTemplateName -> this.setupFrame(resources.getIdentifier(fragmentTemplateName, "layout", packageName))
        }
    }

    private fun fillDataIntoForm() {
        // prefill form with lastly known values if any of them available (unfinished / re-run flow)
        viewModel.formDataJson.observe(this) { formDataJson ->
            (super.fragment?.view as? ViewGroup)?.let { form ->
                ServicePointSetupFormsPopulator.deserializeJsonAndPopulateFields(form, formDataJson)
            }
        }
    }

    /**
     *  Drops the code resolved from the EAN into the form, leaving it visible and editable rather
     *  than saving it behind the user's back.
     */
    private fun applyResolvedCode(code: String) {
        val form = super.fragment?.view as? ViewGroup ?: return
        form.findViewById<EditText>(R.id.service_point_setup_cz_cez_code)?.setText(code)
        Logger.i("Filled HDO code $code resolved from EAN into the setup form")
    }

    private fun isFormFilledIn(): Boolean {
        val form = super.fragment?.view as? ViewGroup ?: return false
        val codeField = form.findViewById<EditText>(R.id.service_point_setup_cz_cez_code)
            ?: return true // provider forms without a code field are not ours to validate

        return codeField.text.toString().isNotBlank()
    }

    private fun populateAndSaveFormData() {
        val data: String = ServicePointSetupFormsPopulator.extractDataFromForm(super.fragment?.view as ViewGroup)
        Logger.i("Obtained end jsoned data for service point setup $data")
        viewModel.updateServicePointData(data) {
            // send event after data have been serialized and saved
            EventBus.getDefault().post(ServicePointEvent(ServicePointEvent.EventType.WIZARD_FLOW_FINISHED))
        }
    }
}
