package com.github.whyrising.flashyalarm.alarmservice.ledcontrols

import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager

internal fun disableTorch(cameraManager: CameraManager) {
  try {
    cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
  } catch (e: CameraAccessException) {
    throw e
  }
}

internal fun enableTorch(cameraManager: CameraManager) {
  try {
    cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
  } catch (e: CameraAccessException) {
    throw e
  }
}
