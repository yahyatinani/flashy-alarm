package com.github.whyrising.flashyalarm.alarmservice

import android.app.Service
import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.util.Log
import com.github.whyrising.flashyalarm.alarmservice.Ids.turnOffLED
import com.github.whyrising.flashyalarm.alarmservice.Ids.turnOnLED
import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.flashpattern.LightPattern
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.BLINK
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.SIGNAL
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.STATIC
import com.github.whyrising.recompose.fx.FxIds.fx
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

interface FrequencyLedController {
  fun on(frequency: Long)
}

interface StaticLedController {
  fun on()
}

interface LedController {
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

class SignalController(private val cameraManager: CameraManager) :
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
        delay(frequency)
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

class StaticControllerImp(private val cameraManager: CameraManager) :
  LedController, StaticLedController {
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
  val staticController = StaticControllerImp(cm)
  val signalController = SignalController(cm)
  regFx(id = turnOnLED) { pair ->
    val (pattern, frequency) = pair as Pair<LightPattern, Long>
    when (pattern) {
      STATIC -> staticController.on()
      BLINK -> blinkController.on(frequency)
      SIGNAL -> signalController.on(frequency)
    }
  }

  regFx(id = turnOffLED) { pattern ->
    when (pattern as LightPattern) {
      STATIC -> staticController.off()
      BLINK -> blinkController.off()
      SIGNAL -> signalController.off()
    }
  }

  regEventFx(id = turnOnLED) { cofx, _ ->
    val appDp = cofx[Schema.db] as AppDb
    val lightPatternsDb = appDp.lightPatternsDb
    val pair = Pair(
      lightPatternsDb.selectedLightPattern,
      lightPatternsDb.blinkFrequency
    )
    m(fx to v(v(turnOnLED, pair)))
  }

  regEventFx(id = turnOffLED) { cofx, _ ->
    val appDp = cofx[Schema.db] as AppDb
    m(fx to v(v(turnOffLED, appDp.lightPatternsDb.selectedLightPattern)))
  }
}
