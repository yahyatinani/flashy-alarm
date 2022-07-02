package com.github.whyrising.flashyalarm.alarmservice

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.regSub

fun regSubs() {
  regSub<AppDb, Boolean>(
    queryId = Ids.isFlashServiceRunning,
  ) { db, _ ->
    db.alarmListenerDb.isFlashServiceRunning
  }

  regSub<AppDb, Boolean>(
    queryId = Ids.isFlashHardwareAvailable,
  ) { db, _ ->
    db.alarmListenerDb.isFlashSupported
  }
}
