package space.kovo.nocnyprud2.ui.activities.wizard

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import space.kovo.nocnyprud2.R
import space.kovo.nocnyprud2.ui.activities.timetable.TimetableActivity
import space.kovo.nocnyprud2.ui.utils.AppPermissions

/**
 *  Last wizard step: asks for everything the app needs to warn about the tariff while it is
 *  closed. All three are optional in the sense that the user may refuse and still use the app -
 *  they just progressively lose the background behaviour, so nothing here blocks finishing.
 */
class PermissionsActivity : WizardActivityBase<TimetableActivity>(
    R.string.permissions_title,
    R.string.permissions_text,
    R.string.permissions_button_next,
    R.layout.wizard_permissions_fragment,
    TimetableActivity::class.java
) {

    private val notificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { this.refreshStatuses() }

    override fun onFormInserted() {

        findViewById<Button>(R.id.permissionsNotificationsButton)
            ?.setOnClickListener { this.requestNotifications() }

        findViewById<Button>(R.id.permissionsAlarmsButton)
            ?.setOnClickListener {
                AppPermissions.exactAlarmSettingsIntent(this)?.let { startActivity(it) }
            }

        findViewById<Button>(R.id.permissionsBatteryButton)
            ?.setOnClickListener {
                startActivity(AppPermissions.batteryOptimizationSettingsIntent())
            }

        this.refreshStatuses()
    }

    override fun onResume() {
        super.onResume()
        // the settings screens are separate activities, so what was granted is only known on return
        this.refreshStatuses()
    }

    private fun requestNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            return
        }
        // below API 33 there is no runtime permission, notifications can only be switched off
        // by the user in settings
        startActivity(AppPermissions.notificationSettingsIntent(this))
    }

    private fun refreshStatuses() {
        val form = super.fragment?.view as? ViewGroup ?: return

        setStatus(form, R.id.permissionsNotificationsStatus, R.string.permissions_notifications_title,
            AppPermissions.canPostNotifications(this))
        setStatus(form, R.id.permissionsAlarmsStatus, R.string.permissions_alarms_title,
            AppPermissions.canScheduleExactAlarms(this))
        setStatus(form, R.id.permissionsBatteryStatus, R.string.permissions_battery_title,
            AppPermissions.isIgnoringBatteryOptimizations(this))

        setButtonEnabled(form, R.id.permissionsNotificationsButton,
            !AppPermissions.canPostNotifications(this))
        setButtonEnabled(form, R.id.permissionsAlarmsButton,
            !AppPermissions.canScheduleExactAlarms(this))
        setButtonEnabled(form, R.id.permissionsBatteryButton,
            !AppPermissions.isIgnoringBatteryOptimizations(this))
    }

    private fun setStatus(form: ViewGroup, viewId: Int, labelResId: Int, granted: Boolean) {
        val marker = getString(
            if (granted) R.string.permissions_granted else R.string.permissions_not_granted)
        form.findViewById<TextView>(viewId)?.text = "${getString(labelResId)} - $marker"
    }

    private fun setButtonEnabled(form: ViewGroup, viewId: Int, enabled: Boolean) {
        form.findViewById<Button>(viewId)?.apply {
            isEnabled = enabled
            setText(if (enabled) R.string.permissions_button_allow else R.string.permissions_button_allowed)
        }
    }
}
