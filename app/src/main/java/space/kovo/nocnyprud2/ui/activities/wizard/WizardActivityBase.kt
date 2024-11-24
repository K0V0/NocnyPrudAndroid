package space.kovo.nocnyprud2.ui.activities.wizard

import android.app.FragmentTransaction
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.ui.activities.BaseActivity
import space.kovo.nocnyprud2.ui.utils.handleMoveToNextActivityButton
import space.kovo.nocnyprud2.ui.utils.setText

abstract class WizardActivityBase<NEXT_ACTIVITY : AppCompatActivity>(
    val titleResourceId: Int,
    val textResourceId: Int,
    val buttonLabelResourceId: Int,
    var fragmentLayoutId: Int,
    val nextActivity: Class<NEXT_ACTIVITY>
) : BaseActivity() {

    protected var fragment: WizardFragmentBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wizard_base)
        this.setupActivity()
        this.setupFrame()
    }

    private fun setupActivity() {
        // UI elenents on base layout
        setText(R.id.wizardBaseTitle, titleResourceId)
        setText(R.id.wizardBaseText, textResourceId)
        setText(R.id.wizardBaseButtonNext, buttonLabelResourceId)
        handleMoveToNextActivityButton(nextActivity) {
            this.onNextClick()
        }
    }

    private fun setupFrame() {
        this.setupFrame(fragmentLayoutId)
    }

    protected fun setupFrame(fragmentLayoutId: Int) {
        // insert "child" fragment layout instead of placeholder
        this.fragment = WizardFragmentBase(fragmentLayoutId)
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.wizardContentContainer, fragment)
        fragmentTransaction.commit()
    }

    open protected fun onNextClick() {
        // Override and implement me in descendants if needed
    }
}
