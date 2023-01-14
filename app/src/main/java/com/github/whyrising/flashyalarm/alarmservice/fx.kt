package com.github.whyrising.flashyalarm.alarmservice

import android.app.Service
import android.content.Context
import android.hardware.camera2.CameraManager
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.turnOffLED
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.turnOnLED
import com.github.whyrising.flashyalarm.alarmservice.torchcontrol.BlinkController
import com.github.whyrising.flashyalarm.alarmservice.torchcontrol.SineController
import com.github.whyrising.flashyalarm.alarmservice.torchcontrol.StaticController
import com.github.whyrising.flashyalarm.panel.common.AppDb
import com.github.whyrising.flashyalarm.panel.flashpattern.LightPattern
import com.github.whyrising.flashyalarm.panel.flashpattern.LightPattern.BLINK
import com.github.whyrising.flashyalarm.panel.flashpattern.LightPattern.SIGNAL
import com.github.whyrising.flashyalarm.panel.flashpattern.LightPattern.STATIC
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.ids.recompose.db
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun registerFlashlightFxs(context: Context) {
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
