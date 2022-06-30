package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.alarmlistener.AlarmListenerDb
import com.github.whyrising.flashyalarm.alarmlistener.moduleDb
import com.github.whyrising.flashyalarm.flashpattern.LightPattern

data class AppDb(
  val screenTitle: String,
  val isDisableServiceDialogVisible: Boolean = false,
  val alarmListenerDb: AlarmListenerDb = moduleDb,
  val selectedLightPattern: LightPattern = LightPattern.STATIC
)

val appDb = AppDb(screenTitle = "")
