package com.github.whyrising.flashyalarm.alarmservice.ledcontrols

import android.hardware.camera2.CameraManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BlinkController(private val cameraManager: CameraManager) :
  LedController, FrequencyLedController {
  private var isOn = false
  private val scope = CoroutineScope(Dispatchers.IO)

  override fun on(frequency: Long) {
    isOn = true
    scope.launch {
      while (isOn) {
        enableTorch(cameraManager)
        delay(frequency)
        disableTorch(cameraManager)
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
