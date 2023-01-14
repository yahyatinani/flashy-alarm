package com.github.whyrising.flashyalarm.flashpattern

data class LightPatternsDb(
  val selectedLightPattern: LightPattern = LightPattern.STATIC,
  val blinkFrequency: Long = 300,
  val blinkFrequencyDialog: Boolean = false,
  val isTestingFrequency: Boolean = false
)

val defaultLightPatternsDb = LightPatternsDb()
