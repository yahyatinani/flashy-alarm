package com.github.whyrising.flashyalarm.panel.flashpattern

import com.github.whyrising.flashyalarm.alarmlistenerservice.LightPattern

data class LightPatternsDb(
  val selectedLightPattern: LightPattern = LightPattern.STATIC,
  val blinkFrequency: Long = 300,
  val blinkFrequencyDialog: Boolean = false,
  val isTestingFrequency: Boolean = false
)

val defaultLightPatternsDb = LightPatternsDb()
