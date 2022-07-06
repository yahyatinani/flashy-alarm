package com.github.whyrising.flashyalarm.alarmservice.ledcontrols

import android.hardware.camera2.CameraManager

class StaticControllerImp(private val cameraManager: CameraManager) :
  LedController, StaticLedController {
  override fun on() {
    enableTorch(cameraManager)
  }

  override fun off() {
    disableTorch(cameraManager)
  }
}
