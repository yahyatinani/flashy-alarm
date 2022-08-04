package com.github.whyrising.flashyalarm.alarmservice

data class AlarmListenerDb(
  val isFlashServiceRunning: Boolean = false,
  val isFlashSupported: Boolean = false,
)

val moduleDb = AlarmListenerDb()
