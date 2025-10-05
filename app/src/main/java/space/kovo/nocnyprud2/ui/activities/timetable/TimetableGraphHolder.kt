package space.kovo.nocnyprud2.ui.activities.timetable;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet;
import android.widget.LinearLayout
import kotlin.jvm.JvmOverloads;
import androidx.core.graphics.toColorInt

class TimetableGraphHolder @JvmOverloads
constructor(
    context: Context,
    attrs:AttributeSet? = null
) : LinearLayout(context, attrs) {

    val lineCount = 24
    private val hourColumn = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = "#EEEEEE".toColorInt() }

    init {
        setWillNotDraw(false) // Needed for layouts to receive onDraw() calls
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val stepX = width / lineCount.toFloat()
        val stopY = height.toFloat()
        for (i in 0..lineCount) {
            if (i % 2 == 0) {
                continue
            }
            val x = i * stepX
            canvas.drawRect(x, 0f, x + stepX, stopY, hourColumn)
        }
    }
}