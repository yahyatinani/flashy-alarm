package com.github.whyrising.flashyalarm.home

import android.content.Context
import android.content.Intent
import com.github.whyrising.flashyalarm.alarmservice.FlashyAlarmService
import com.github.whyrising.flashyalarm.alarmservice.Ids
import com.github.whyrising.recompose.regFx

fun init(context: Context) {
  regFx(id = Ids.toggleFlashyAlarmService) { serviceFlag ->
    val serviceIntent = Intent(context, FlashyAlarmService::class.java)
    when (serviceFlag as Boolean) {
      true -> serviceIntent.also { intent ->
        context.startService(intent)
      }
      else -> serviceIntent.also { intent ->
        context.stopService(intent)
      }
    }
  }
  regHomeEvents()
  regHomeSubs()
}
