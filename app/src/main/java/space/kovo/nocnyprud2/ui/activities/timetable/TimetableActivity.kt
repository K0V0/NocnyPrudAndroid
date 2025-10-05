package space.kovo.nocnyprud2.ui.activities.timetable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.kovo.nocnyprud2.R

class TimetableActivity : AppCompatActivity() {

    lateinit var timetableRecyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable)
        fillUpTimetable()
    }

    fun fillUpTimetable() {
        findViewById<RecyclerView>(R.id.timetable_graph_timespans_holder).apply {
            setLayoutManager(LinearLayoutManager(this@TimetableActivity))
            setAdapter(TimetableDayItemAdapter(this@TimetableActivity))
        }
    }
}
