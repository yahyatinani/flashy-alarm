package com.github.whyrising.flashyalarm.alarmservice.torchcontrol

import android.hardware.camera2.CameraManager
import kotlinx.coroutines.delay

class SineController(cameraManager: CameraManager) :
  DynamicPatternBase(cameraManager) {

  override suspend fun pattern(frequency: Long) {
    enableTorch(cameraManager)
    delay(frequency)
    disableTorch(cameraManager)
    delay(frequency)
  }
}
