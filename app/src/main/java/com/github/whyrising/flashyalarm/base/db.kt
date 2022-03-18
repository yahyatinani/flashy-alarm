package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.alarmlistener.AlarmListenerDb
import com.github.whyrising.flashyalarm.alarmlistener.moduleDb

data class AppDb(
    val screenTitle: String,
    val isDisableServiceDialogVisible: Boolean = false,
    val alarmListenerDb: AlarmListenerDb = moduleDb
)

val appDb = AppDb(screenTitle = "")
