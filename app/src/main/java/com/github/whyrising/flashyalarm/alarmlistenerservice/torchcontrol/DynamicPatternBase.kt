package com.github.whyrising.flashyalarm.alarmlistenerservice.torchcontrol

import android.hardware.camera2.CameraManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class DynamicPatternBase(val cameraManager: CameraManager) :
  DynamicPattern {
  internal abstract suspend fun pattern(frequency: Long)

  private var isOn = false
  private val scope = CoroutineScope(Dispatchers.IO)

  override fun on(frequency: Long) {
    isOn = true
    scope.launch {
      while (isOn) {
        pattern(frequency)
      }
    }.invokeOnCompletion {
      disableTorch(cameraManager)
      Log.i("LedController", "LED is off.")
    }
  }

  override fun off() {
    isOn = false
  }
}
