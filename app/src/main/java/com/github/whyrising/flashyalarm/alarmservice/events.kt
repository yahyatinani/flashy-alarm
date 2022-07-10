package com.github.whyrising.flashyalarm.alarmservice

import com.github.whyrising.flashyalarm.alarmservice.AlarmService.isFlashHardwareAvailable
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.isFlashServiceRunning
import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.base.base.pushNotification
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema.db
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regEvents() {
  regEventFx(
    id = AlarmService.checkDeviceFlashlight,
    interceptors = v(injectCofx(isFlashHardwareAvailable)),
  ) { cofx, _ ->
    val appDb = cofx[db] as AppDb
    m(
      db to appDb.copy(
        alarmListenerDb = appDb.alarmListenerDb.copy(
          isFlashSupported = cofx[isFlashHardwareAvailable] as Boolean
        )
      )
    )
  }

  regEventFx(
    id = isFlashServiceRunning,
    interceptors = v(injectCofx(isFlashServiceRunning))
  ) { cofx, _ ->
    val appDb = cofx[db] as AppDb
    val newDb = appDb.copy(
      alarmListenerDb = appDb.alarmListenerDb.copy(
        isFlashServiceRunning = cofx[isFlashServiceRunning] as Boolean
      )
    )
    m(db to newDb)
  }

  regEventFx(id = pushNotification) { _, (_, id, title, content) ->
    m(
      pushNotification to m(
        "id" to id,
        "title" to title,
        "content" to content
      )
    )
  }
}
