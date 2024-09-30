package space.kovo.nocnyprud2

import android.app.Activity
import android.os.Bundle
import space.kovo.nocnyprud2.ui.CountrySelectActivity
import space.kovo.nocnyprud2.ui.timetable.TimetableActivity
import space.kovo.nocnyprud2.ui.utils.handleMoveToNextActivity

class MainActivity : Activity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome)
        this.navigateToNextActivity()
    }

    private fun navigateToNextActivity() {
        // TODO
        val servicePointSetUp: Boolean = false;
        if (servicePointSetUp) {
            handleMoveToNextActivity<TimetableActivity>()
        } else {
            handleMoveToNextActivity<CountrySelectActivity>()
        }
    }
}


