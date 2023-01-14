package com.github.whyrising.flashyalarm.panel.home

import com.github.whyrising.flashyalarm.alarmservice.AlarmService.toggleFlashyAlarmService
import com.github.whyrising.flashyalarm.panel.common.AppDb
import com.github.whyrising.flashyalarm.panel.common.common.isAboutDialogVisible
import com.github.whyrising.recompose.fx.FxIds
import com.github.whyrising.recompose.ids.recompose.db
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regHomeEvents() {
  regEventDb<AppDb>(id = home.showDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = true)
  }
  regEventDb<AppDb>(id = home.hideDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = false)
  }
  regEventFx(id = toggleFlashyAlarmService) { cofx, (_, flag) ->
    val appDb = cofx[db] as AppDb
    val newDb = appDb.copy(
      alarmListenerDb = appDb.alarmListenerDb.copy(
        isFlashServiceRunning = flag as Boolean
      )
    )
    m(
      db to newDb,
      FxIds.fx to v(v(toggleFlashyAlarmService, flag))
    )
  }

  regEventDb<AppDb>(id = isAboutDialogVisible) { db, (_, visibility) ->
    db.copy(isAboutDialogVisible = visibility as Boolean)
  }
}
