package space.kovo.nocnyprud2.ui.activities.wizard

import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import space.kovo.nocnyprud2.R

class WizardFragmentBase @SuppressLint("ValidFragment") constructor(private val fragmentLayout: Int = R.layout.wizard_base_fragment) : Fragment() {

    private var container: ViewGroup? = null

    @Deprecated("Deprecated in Java")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragmentLayout, container, false)
        this.container = container
    }

    fun getContainer(): ViewGroup? = container
}
