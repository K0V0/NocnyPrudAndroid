package space.kovo.nocnyprud2.ui.activities.wizard

import android.app.FragmentTransaction
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepository
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.ui.utils.handleMoveToNextActivityButton
import space.kovo.nocnyprud2.ui.utils.setText

abstract class WizardActivityBase<NEXT_ACTIVITY : AppCompatActivity>(
    val titleResourceId: Int,
    val textResourceId: Int,
    val buttonLabelResourceId: Int,
    val fragmentLayoutId: Int,
    val nextActivity: Class<NEXT_ACTIVITY>
) : AppCompatActivity() {

    protected val servicePointRepository: ServicePointRepository = ServicePointRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wizard_base)
        this.setupActivity()
    }

    private fun setupActivity() {

        // UI elenents on base layout
        setText(R.id.wizardBaseTitle, titleResourceId)
        setText(R.id.wizardBaseText, textResourceId)
        setText(R.id.wizardBaseButtonNext, buttonLabelResourceId)
        handleMoveToNextActivityButton(nextActivity)

        // insert "child" fragment layout instead of placeholder
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.contentContainer, WizardFragmentBase(fragmentLayoutId))
        fragmentTransaction.commit()
    }
}
