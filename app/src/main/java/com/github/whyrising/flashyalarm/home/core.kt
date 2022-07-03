package com.github.whyrising.flashyalarm.home

import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.github.whyrising.flashyalarm.alarmservice.FlashyAlarmService
import com.github.whyrising.flashyalarm.alarmservice.Ids
import com.github.whyrising.flashyalarm.alarmservice.dataStore
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

fun init(context: Context) {
  regFx(id = Ids.toggleFlashyAlarmService) { serviceFlag ->
    val serviceIntent = Intent(context, FlashyAlarmService::class.java)
    when (serviceFlag as Boolean) {
      true -> serviceIntent.also { intent ->
        context.startService(intent)
        saveServiceStatus(context, true)
      }
      else -> serviceIntent.also { intent ->
        context.stopService(intent)
        saveServiceStatus(context, false)
      }
    }
  }
  regHomeEvents()
  regHomeSubs()
}
