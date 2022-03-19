package com.github.whyrising.flashyalarm.alarmlistener

data class AlarmListenerDb(
    val isNotifAccessEnabled: Boolean = false,
    val isFlashSupported: Boolean = false,
)

val moduleDb = AlarmListenerDb()
