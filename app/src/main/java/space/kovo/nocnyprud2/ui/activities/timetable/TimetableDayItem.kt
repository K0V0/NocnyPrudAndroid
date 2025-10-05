package space.kovo.nocnyprud2.ui.activities.timetable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TimetableDayItem(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var timespans: List<Pair<Float, Float>> = emptyList()
        set(value) {
            field = value
            invalidate() // redraw when data changes
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4A90E2")
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val hourWidth = width / 24f
        val barHeight = height.toFloat()

        for ((start, end) in timespans) {
            val left = start * hourWidth
            val right = end * hourWidth
            canvas.drawRect(left, 0f, right, barHeight, paint)
        }
    }
}