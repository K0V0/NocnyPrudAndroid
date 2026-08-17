package space.kovo.nocnyprud2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import space.kovo.nocnyprud2.backend.repositories.database.ServicePointRepositoryImpl
import space.kovo.nocnyprud2.ui.activities.timetable.TimetableActivity
import space.kovo.nocnyprud2.ui.activities.wizard.WelcomeActivity
import space.kovo.nocnyprud2.ui.utils.moveToActivity

/**
 *  Entry point that only decides where the user belongs: straight to the timetable once the
 *  service point is set up, into the setup wizard while it is not.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.navigateToNextActivity()
    }

    private fun navigateToNextActivity() {
        lifecycleScope.launch {
            val servicePointSetUp: Boolean = ServicePointRepositoryImpl.getInstance()
                .isDefaultServicePointSetUp()

            if (servicePointSetUp) {
                moveToActivity<TimetableActivity>()
            } else {
                moveToActivity<WelcomeActivity>()
            }
            // this screen is a router with nothing to come back to - leaving it on the stack
            // would send the user right back here when they navigate back
            finish()
        }
    }
}
