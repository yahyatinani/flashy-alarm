package com.github.whyrising.flashyalarm.alarmservice

import android.app.Service
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.util.Log
import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.flashpattern.LightPattern
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.BLINK
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.STATIC
import com.github.whyrising.recompose.fx.FxIds
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.schemas.Schema
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface LedController {
  fun on()
  fun off()
}

private fun disableTorch(cameraManager: CameraManager) {
  try {
    cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
  } catch (e: CameraAccessException) {
    throw e
  }
}

private fun enableTorch(cameraManager: CameraManager) {
  try {
    cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
  } catch (e: CameraAccessException) {
    throw e
  }
}

class BlinkController(private val cameraManager: CameraManager) :
  LedController {
  private var isOn = false

  private val scope = CoroutineScope(Dispatchers.IO)

  override fun on() {
    isOn = true
    scope.launch {
      while (isOn) {
        enableTorch(cameraManager)
        delay(300)
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

class StaticController(private val cameraManager: CameraManager) :
  LedController {
  override fun on() {
    enableTorch(cameraManager)
  }

  override fun off() {
    disableTorch(cameraManager)
  }
}

fun registerFlashlightFxs(context: Context) {
  val cm = context.getSystemService(Service.CAMERA_SERVICE) as CameraManager
  val blinkController = BlinkController(cm)
  val staticController = StaticController(cm)
  regFx(id = Ids.turnOnLED) { pattern ->
    when (pattern as LightPattern) {
      STATIC -> staticController.on()
      BLINK -> blinkController.on()
    }
  }

  regFx(id = Ids.turnOffLED) { pattern ->
    when (pattern as LightPattern) {
      STATIC -> staticController.off()
      BLINK -> blinkController.off()
    }
  }

  regEventFx(id = Ids.turnOnLED) { cofx, _ ->
    val appDp = cofx[Schema.db] as AppDb
    m(FxIds.fx to v(v(Ids.turnOnLED, appDp.selectedLightPattern)))
  }

  regEventFx(id = Ids.turnOffLED) { cofx, _ ->
    val appDp = cofx[Schema.db] as AppDb
    m(FxIds.fx to v(v(Ids.turnOffLED, appDp.selectedLightPattern)))
  }
}
