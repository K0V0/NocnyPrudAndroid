package space.kovo.nocnyprud2.ui.activities.timetable

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.backend.alarms.TariffAlarmScheduler
import space.kovo.nocnyprud2.backend.services.TimetableServiceImpl

class TimetableActivity : AppCompatActivity() {

    lateinit var timetableRecyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timetable)
        fillUpTimetable()
        refreshIfRunningOut()
    }

    fun fillUpTimetable() {
        findViewById<RecyclerView>(R.id.timetable_graph_timespans_holder).apply {
            setLayoutManager(LinearLayoutManager(this@TimetableActivity))
            setAdapter(TimetableDayItemAdapter(this@TimetableActivity))
        }
    }

    /**
     *  Safety net for the background refresh: phones that manage battery aggressively may never
     *  let the periodic worker run, so opening the app is also a chance to top the timetable up.
     *  The screen is filled from the database either way, this only replaces it once done.
     */
    private fun refreshIfRunningOut() {
        lifecycleScope.launch {
            val service = TimetableServiceImpl.getInstance()
            try {
                if (service.isTimetableRunningOut()) {
                    Logger.i("Stored timetable is running out, refreshing on app start")
                    withContext(Dispatchers.IO) { service.refreshTimetable(applicationContext) }
                } else {
                    // still make sure an alarm is pending - it may have been lost to a reboot
                    // that happened before the app was ever opened again
                    withContext(Dispatchers.IO) {
                        TariffAlarmScheduler.getInstance()
                            .rearmFromStoredTimetable(applicationContext)
                    }
                }
            } catch (e: Exception) {
                // an unreachable provider must not stop the stored timetable from being shown
                Logger.e(e, "Could not refresh the timetable on app start")
            }
        }
    }
}
