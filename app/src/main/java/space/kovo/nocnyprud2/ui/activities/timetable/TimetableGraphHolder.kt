package space.kovo.nocnyprud2.ui.activities.timetable;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.graphics.toColorInt
import java.time.LocalTime
import kotlin.math.ceil


class TimetableGraphHolder @JvmOverloads
constructor(
    context: Context,
    attrs:AttributeSet? = null
) : LinearLayout(context, attrs) {

    val lineCount = 24
    private val hourColumn = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = "#EEEEEE".toColorInt() }
    private val hourPointerColumn = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = "#ff0000".toColorInt() }

    init {
        setWillNotDraw(false) // Needed for layouts to receive onDraw() calls
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHoursColums(canvas)
        drawActualTimeBar(canvas)
    }

    fun drawHoursColums(canvas: Canvas) {
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

    fun drawActualTimeBar(canvas: Canvas) {
        val stopY = height.toFloat()
        val x = ceil(width * getDayFraction())
        val timeBarWidth = (width / 24f / 8f).coerceAtLeast(2f)
        val timeBarCenteringOffset = timeBarWidth / 2f
        canvas.drawRect(
            x - timeBarCenteringOffset,
            0f,
            x + timeBarWidth - timeBarCenteringOffset,
            stopY,
            hourPointerColumn)
    }

    private fun getDayFraction(): Float {
        val now = LocalTime.now()
        val totalMinutes = now.hour * 60 + now.minute
        return (totalMinutes / 1440f)
    }
}