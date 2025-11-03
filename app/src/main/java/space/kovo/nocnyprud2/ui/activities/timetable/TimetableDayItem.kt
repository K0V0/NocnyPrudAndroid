package space.kovo.nocnyprud2.ui.activities.timetable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import space.kovo.nocnyprud2.backend.dtos.timetable.TimespanDto

class TimetableDayItem(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var timespans: List<TimespanDto> = emptyList()
        set(value) {
            field = value
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4A90E2")
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val hourWidth = width / 24f
        val barHeight = height.toFloat()

        for (timespan in timespans) {
            val left = timespan.startTimeDecimal * hourWidth
            val right = timespan.endTimeDecimal * hourWidth
            canvas.drawRect(left, 0f, right, barHeight, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val hourWidth = width / 24f
            val clickedHour = event.x / hourWidth

            for (timespan in timespans) {
                if (clickedHour in timespan.startTimeDecimal..timespan.endTimeDecimal) {
                    Toast.makeText(
                        context,
                        "Clicked timespan: $timespan.startTime–$timespan.endTime",
                        Toast.LENGTH_SHORT)
                        .show()
                    //TODO zobrazit casy
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }
}