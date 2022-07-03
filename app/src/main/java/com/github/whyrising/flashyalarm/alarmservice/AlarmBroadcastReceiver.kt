package com.github.whyrising.flashyalarm.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.github.whyrising.flashyalarm.alarmservice.Ids.turnOffLED
import com.github.whyrising.flashyalarm.alarmservice.Ids.turnOnLED
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.y.core.hs
import com.github.whyrising.y.core.v

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
  name = "settings"
)

class AlarmBroadcastReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (intent == null) return
    val action = intent.action
    Log.i("AlarmBroadcastReceiver", "$action")

    if (alarmAlertActions.contains(action)) {
      dispatch(v(turnOnLED))
    } else if (alarmStoppedActions.contains(action)) {
      dispatch(v(turnOffLED))
    } else TODO("This action is not supported: $action")
  }

  companion object {
    // val ALARM_DISMISS_ACTION =
    // "com.samsung.sec.android.clockpackage.alarm.ACTION_DISMISS_ALARM_ROUTINE"
    // val ALARM_ALERT_STOP_ACTION =
    // "com.samsung.sec.android.clockpackage.alarm.ACTION_LOCAL_ALARM_ALERT_STOP"
    // "com.android.deskclock.ALARM_DISMISS",
    // "com.android.deskclock.ALARM_SNOOZE"

    val alarmAlertActions = hs(
      "com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT",
      "com.android.deskclock.ALARM_ALERT",
    )
    val alarmStoppedActions = hs(
      "com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT",
      "com.android.deskclock.ALARM_DONE",
    )
    val filter = IntentFilter().apply {
      alarmAlertActions.forEach { addAction(it) }
      alarmStoppedActions.forEach { addAction(it) }
    }
  }
}
