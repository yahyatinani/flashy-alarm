package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.alarmservice.Ids.toggleFlashyAlarmService
import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.base.Ids.isAboutDialogVisible
import com.github.whyrising.recompose.fx.FxIds
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regHomeEvents() {
  regEventDb<AppDb>(id = Ids.showDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = true)
  }
  regEventDb<AppDb>(id = Ids.hideDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = false)
  }
  regEventFx(id = toggleFlashyAlarmService) { cofx, (_, flag) ->
    val appDb = cofx[Schema.db] as AppDb
    val newDb = appDb.copy(
      alarmListenerDb = appDb.alarmListenerDb.copy(
        isFlashServiceRunning = flag as Boolean
      )
    )
    m(
      Schema.db to newDb,
      FxIds.fx to v(v(toggleFlashyAlarmService, flag))
    )
  }

  regEventDb<AppDb>(id = isAboutDialogVisible) { db, (_, visibility) ->
    db.copy(isAboutDialogVisible = visibility as Boolean)
  }
}
