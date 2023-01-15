package com.github.whyrising.flashyalarm.alarmlistenerservice

import android.app.Service
import android.content.Context
import android.hardware.camera2.CameraManager
import com.github.whyrising.flashyalarm.alarmlistenerservice.AlarmListenerService.turnOffLED
import com.github.whyrising.flashyalarm.alarmlistenerservice.AlarmListenerService.turnOnLED
import com.github.whyrising.flashyalarm.alarmlistenerservice.LightPattern.BLINK
import com.github.whyrising.flashyalarm.alarmlistenerservice.LightPattern.SIGNAL
import com.github.whyrising.flashyalarm.alarmlistenerservice.LightPattern.STATIC
import com.github.whyrising.flashyalarm.alarmlistenerservice.torchcontrol.BlinkController
import com.github.whyrising.flashyalarm.alarmlistenerservice.torchcontrol.SineController
import com.github.whyrising.flashyalarm.alarmlistenerservice.torchcontrol.StaticController
import com.github.whyrising.flashyalarm.panel.common.AppDb
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.ids.recompose.db
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun registerTorchFx(context: Context) {
  val cm = context.getSystemService(Service.CAMERA_SERVICE) as CameraManager
  val blinkController = BlinkController(cm)
  val staticController = StaticController(cm)
  val signalController = SineController(cm)

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
    val appDp = cofx[db] as AppDb
    val lightPatternsDb = appDp.lightPatternsDb
    val pair = Pair(
      lightPatternsDb.selectedLightPattern,
      lightPatternsDb.blinkFrequency
    )
    m(fx to v(v(turnOnLED, pair)))
  }

  regEventFx(id = turnOffLED) { cofx, _ ->
    val appDp = cofx[db] as AppDb
    m(fx to v(v(turnOffLED, appDp.lightPatternsDb.selectedLightPattern)))
  }
}
