package space.kovo.nocnyprud2

import android.app.Activity
import android.os.Bundle
import space.kovo.nocnyprud2.ui.activities.timetable.TimetableActivity
import space.kovo.nocnyprud2.ui.activities.wizard.WelcomeActivity
import space.kovo.nocnyprud2.ui.utils.moveToActivity

class MainActivity : Activity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.navigateToNextActivity()
    }

    private fun navigateToNextActivity() {
        // TODO
        val servicePointSetUp: Boolean = false;
        if (servicePointSetUp) {
            moveToActivity<TimetableActivity>()
        } else {
            moveToActivity<WelcomeActivity>()
        }
    }
}


