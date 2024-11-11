package space.kovo.nocnyprud2.ui.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import space.kovo.nocnyprud2.R

inline fun <reified T: Activity> Activity.handleMoveToNextActivityButton() {
    this.findViewById<Button>(R.id.wizardBaseButtonNext)
        .setOnClickListener { startActivity(Intent(this, T::class.java)) }
}

fun <T: Activity> Activity.handleMoveToNextActivityButton(
    nextActivityClass: Class<T>,
    listenerRunnable: () -> Unit
) {
    this.findViewById<Button>(R.id.wizardBaseButtonNext)
        .setOnClickListener {
            listenerRunnable()
            startActivity(Intent(this, nextActivityClass))
        }
}

inline fun <reified T: Activity> Activity.moveToActivity() {
    startActivity(Intent(this, T::class.java))
}

fun Activity.setText(componentId: Int, textResourceId: Int) {
    val component: View = findViewById(componentId)
    val text: String = getString(textResourceId)

    when (component) {
        is TextView -> component.setText(text)
        is Button -> component.setText(text)
    }
}


