package com.github.whyrising.flashyalarm.alarmlistenerservice.torchcontrol

import android.hardware.camera2.CameraManager
import kotlinx.coroutines.delay

class BlinkController(cameraManager: CameraManager) :
  DynamicPatternBase(cameraManager) {
  override suspend fun pattern(frequency: Long) {
    enableTorch(cameraManager)
    delay(frequency)
    disableTorch(cameraManager)
  }
}
