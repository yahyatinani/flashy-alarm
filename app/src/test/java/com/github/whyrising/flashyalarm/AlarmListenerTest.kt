package com.github.whyrising.flashyalarm

import androidx.compose.animation.ExperimentalAnimationApi
import com.github.whyrising.flashyalarm.alarmlistener.AlarmListener
import com.github.whyrising.flashyalarm.alarmlistener.Ids.flashOn
import com.github.whyrising.flashyalarm.alarmlistener.SAMSUNG_ALARM_PKG
import com.github.whyrising.flashyalarm.alarmlistener.flashlightEffect
import com.github.whyrising.y.core.m
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@ExperimentalAnimationApi
class AlarmListenerTest : FreeSpec({
  "TAG" {
    AlarmListener().TAG shouldBe "AlarmListener"
  }

  "flashlightEffect()" - {
    "when category, groupKey not of alarm alert, return empty effects map" {
      flashlightEffect(
        pkgName = SAMSUNG_ALARM_PKG,
        group = "TIMER_GROUP_KEY",
        category = "timer"
      ) shouldBe m()

      flashlightEffect(
        pkgName = SAMSUNG_ALARM_PKG,
        category = "alarm",
        group = "TIMER_GROUP_KEY"
      ) shouldBe m()

      flashlightEffect(
        pkgName = SAMSUNG_ALARM_PKG,
        category = null,
        group = "TIMER_GROUP_KEY"
      ) shouldBe m()

      flashlightEffect(
        pkgName = SAMSUNG_ALARM_PKG,
        category = "alarm",
        group = null
      ) shouldBe m()

      flashlightEffect(
        pkgName = SAMSUNG_ALARM_PKG,
        category = null,
        group = null
      ) shouldBe m()
    }

    "when category, groupKey are of alarm alert, return `flash_on` effect" {
      flashlightEffect(
        pkgName = SAMSUNG_ALARM_PKG,
        group = "ALARM_GROUP_KEY",
        category = "alarm",
      ) shouldBe m(flashOn to true)

      flashlightEffect(
        pkgName = "com.android.deskclock",
        group = "",
        category = "alarm",
      ) shouldBe m(flashOn to true)
    }
  }

  "AlarmListener classpath for AndroidManifest" {
    AlarmListener::class.java.name shouldBe
      "com.github.whyrising.flashyalarm.alarmlistener.AlarmListener"
  }
})
