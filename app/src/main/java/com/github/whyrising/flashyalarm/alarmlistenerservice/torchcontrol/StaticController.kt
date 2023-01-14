package com.github.whyrising.flashyalarm.alarmlistenerservice.torchcontrol

import android.hardware.camera2.CameraManager

class StaticController(private val cameraManager: CameraManager) :
  StaticPattern {
  override fun on() {
    enableTorch(cameraManager)
  }

  override fun off() {
    disableTorch(cameraManager)
  }
}
