package space.kovo.nocnyprud2.ui.activities.timetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import space.kovo.nocnyprud2.backend.dtos.timetable.TimespanDto
import space.kovo.nocnyprud2.backend.dtos.timetable.TimetableDto
import space.kovo.nocnyprud2.ui.viewModels.timetable.TimetableViewModel
import space.kovo.nocnyprud2.R

class TimetableDayItemAdapter(
    timetableActivity: TimetableActivity
) : RecyclerView.Adapter<TimetableDayItemAdapter.TimelineViewHolder>() {

    class TimelineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayName: TextView = view.findViewById(R.id.dayName)
        val dayTimeline: TimetableDayItem = view.findViewById(R.id.timetableDay)
    }

    private val viewModel: TimetableViewModel by timetableActivity.viewModels()

    private val timetableDays = HashMap<String, List<TimespanDto>>()
    private val timetableDayTitles = ArrayList<String>()

    init {
        viewModel.timetable.observe(timetableActivity) {
            timeable -> this.updateItems(timeable)
            super.notifyDataSetChanged()
        }
    }

    private fun updateItems(timetable: TimetableDto) {
        for (day in timetable.timetable) {
            timetableDays.put(day.date, day.timespans)
            timetableDayTitles.add(day.date)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.timetable_day, parent, false)

        view.findViewById<TextView>(R.id.dayName).setText(timetableDayTitles[viewType])

        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TimelineViewHolder,
        position: Int
    ) {
        val dayName: String = timetableDayTitles[position]
        holder.dayName.text = dayName
        holder.dayTimeline.timespans = timetableDays[dayName].orEmpty()
    }

    override fun getItemCount(): Int {
        return timetableDays.size
    }
}