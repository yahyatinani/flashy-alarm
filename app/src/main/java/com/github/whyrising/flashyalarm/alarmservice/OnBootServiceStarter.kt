package com.github.whyrising.flashyalarm.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.whyrising.flashyalarm.panel.home.IS_FLASHY_SERVICE_ENABLED
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.y.core.v
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

const val BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED"
const val DISABLE_SERVICE = "disable_flash_service"

class OnBootServiceStarter : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context == null || intent == null) return

    if (intent.extras?.getBoolean(DISABLE_SERVICE) == true) {
      dispatch(v(AlarmService.toggleFlashyAlarmService, false))
    } else if (intent.action == BOOT_COMPLETED_ACTION) {
      val isEnabled = runBlocking {
        context.dataStore.data.map { preferences ->
          preferences[IS_FLASHY_SERVICE_ENABLED]
        }.first()
      }
      if (isEnabled != null && isEnabled == true) {
        context.startService(Intent(context, FlashyAlarmService::class.java))
      }
    }
  }
}
