package com.github.whyrising.flashyalarm.panel.home

import com.github.whyrising.flashyalarm.panel.common.AppDb
import com.github.whyrising.flashyalarm.panel.common.common.isAboutDialogVisible
import com.github.whyrising.flashyalarm.panel.home.home.hideDisableServiceDialog
import com.github.whyrising.flashyalarm.panel.home.home.showDisableServiceDialog
import com.github.whyrising.flashyalarm.panel.home.home.toggleFlashyAlarmService
import com.github.whyrising.recompose.fx.FxIds
import com.github.whyrising.recompose.ids.recompose.db
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regHomeEvents() {
  regEventDb<AppDb>(id = showDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = true)
  }
  regEventDb<AppDb>(id = hideDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = false)
  }
  regEventFx(id = toggleFlashyAlarmService) { cofx, (_, flag) ->
    val appDb = cofx[db] as AppDb
    val newDb = appDb.copy(isAlarmListenerRunning = flag as Boolean)
    m(
      db to newDb,
      FxIds.fx to v(v(toggleFlashyAlarmService, flag))
    )
  }

  regEventDb<AppDb>(id = isAboutDialogVisible) { db, (_, visibility) ->
    db.copy(isAboutDialogVisible = visibility as Boolean)
  }
}
