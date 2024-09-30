package space.kovo.nocnyprud2.ui.utils

import android.app.Activity
import android.content.Intent
import android.widget.Button
import space.kovo.nocnyprud2.R

inline fun <reified T: Activity> Activity.handleMoveToNextActivity() {
    this.findViewById<Button>(R.id.buttonNext)
        .setOnClickListener { startActivity(Intent(this, T::class.java)) }
}


