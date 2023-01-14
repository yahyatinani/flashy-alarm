package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.alarmservice.AlarmListenerDb
import com.github.whyrising.flashyalarm.alarmservice.moduleDb
import com.github.whyrising.flashyalarm.flashpattern.LightPatternsDb
import com.github.whyrising.flashyalarm.flashpattern.defaultLightPatternsDb

data class AppDb(
  val screenTitle: String,
  val isDisableServiceDialogVisible: Boolean = false,
  val alarmListenerDb: AlarmListenerDb = moduleDb,
  val lightPatternsDb: LightPatternsDb = defaultLightPatternsDb,
  val isAboutDialogVisible: Boolean = false,
  val isBackstackAvailable: Boolean = false
)

val appDb = AppDb(screenTitle = "")
