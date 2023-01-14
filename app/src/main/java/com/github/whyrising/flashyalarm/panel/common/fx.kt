package com.github.whyrising.flashyalarm.panel.common

import com.github.whyrising.flashyalarm.panel.common.common.exitApp
import com.github.whyrising.recompose.regFx
import kotlin.system.exitProcess

const val CHANNEL_ID = "flashlight_service_notification_channel"
const val CHANNEL_NAME = "Flashy Alarm Notification Channel"

fun regBaseFx() {
  regFx(id = exitApp) {
    exitProcess(-1)
  }
}
