package com.github.whyrising.flashyalarm.panel.home

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.github.whyrising.flashyalarm.alarmlistenerservice.FlashyAlarmService
import com.github.whyrising.flashyalarm.alarmlistenerservice.dataStore
import com.github.whyrising.flashyalarm.panel.common.common.isAlarmListenerRunning
import com.github.whyrising.recompose.cofx.regCofx
import com.github.whyrising.recompose.regFx
import kotlinx.coroutines.runBlocking

val IS_FLASHY_SERVICE_ENABLED = booleanPreferencesKey("isFlashyServiceEnabled")

fun saveServiceStatus(context: Context, b: Boolean) {
  runBlocking {
    context.dataStore.edit { settings ->
      settings[IS_FLASHY_SERVICE_ENABLED] = b
    }
  }
}

fun regHomeFx(context: Context) {
  regCofx(id = isAlarmListenerRunning) { coeffects ->
    coeffects.assoc(
      isAlarmListenerRunning,
      FlashyAlarmService.isServiceRunning
    )
  }
  regFx(id = home.toggleFlashyAlarmService) { serviceFlag ->
    val serviceIntent =
      Intent(context, FlashyAlarmService::class.java)
    when (serviceFlag as Boolean) {
      true -> context.startService(serviceIntent)
      else -> context.stopService(serviceIntent)
    }
    saveServiceStatus(context, serviceFlag)
  }
}
