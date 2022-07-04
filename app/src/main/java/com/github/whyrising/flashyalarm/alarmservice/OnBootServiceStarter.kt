package com.github.whyrising.flashyalarm.alarmservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.whyrising.flashyalarm.home.IS_FLASHY_SERVICE_ENABLED
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class OnBootServiceStarter : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context == null || intent == null) return

    if (intent.action == "android.intent.action.BOOT_COMPLETED") {
      val isEnabled = runBlocking {
        context.dataStore.data.map { preferences ->
          preferences[IS_FLASHY_SERVICE_ENABLED]
        }.first()
      }
      if (isEnabled != null && isEnabled == true)
        context.startService(Intent(context, FlashyAlarmService::class.java))
    }
  }
}
