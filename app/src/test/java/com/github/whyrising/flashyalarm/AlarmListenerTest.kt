package com.github.whyrising.flashyalarm

import androidx.compose.animation.ExperimentalAnimationApi
import com.github.whyrising.flashyalarm.alarmlistener.AlarmListener
import com.github.whyrising.flashyalarm.alarmlistener.Ids
import com.github.whyrising.flashyalarm.alarmlistener.flashlightOn
import com.github.whyrising.y.collections.core.m
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@ExperimentalAnimationApi
class AlarmListenerTest : FreeSpec({
    "TAG" {
        AlarmListener().TAG shouldBe "AlarmListener"
    }

    "flashlightOn()" - {
        "when category, groupKey not of alarm alert, return empty effects map" {
            flashlightOn("timer", "TIMER_GROUP_KEY") shouldBe m()
            flashlightOn("timer", "ALARM_GROUP_KEY") shouldBe m()
            flashlightOn("alarm", "TIMER_GROUP_KEY") shouldBe m()
            flashlightOn(null, null) shouldBe m()
        }

        "when category, groupKey are of alarm alert, return `flash_on` effect" {
            flashlightOn(
                "alarm",
                "ALARM_GROUP_KEY"
            ) shouldBe m(Ids.flashOn to true)
        }
    }

    "AlarmListener classpath for AndroidManifest" {
        AlarmListener::class.java.name shouldBe
            "com.github.whyrising.flashyalarm.alarmlistener.AlarmListener"
    }
})
