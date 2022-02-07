package com.github.whyrising.flashyalarm

import com.github.whyrising.flashyalarm.Ids.flash_on
import com.github.whyrising.y.collections.core.m
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

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
            ) shouldBe m(flash_on to true)
        }

        // groupKey : ALARM_GROUP_KEY
        /*
            StatusBarNotification(
                pkg=com.sec.android.app.clockpackage
                user=UserHandle{0}
                id=2147483645
                tag=null
                key=0|com.sec.android.app.clockpackage|2147483645|null|10178:
                    Notification(
                        channel=notification_channel_timer
                        shortcut=null
                        contentView=com.sec.android.app.clockpackage/0x7f0c005c
                        vibrate=null
                        sound=null
                        defaults=0x0
                        flags=0x2
                        color=0xff8587fe
                        category=alarm
                        groupKey=TIMER_GROUP_KEY
                        actions=2
                        vis=PRIVATE
                        semFlags=0x0
                        semPriority=0
                        semMissedCount=0))
        */

        /*
            StatusBarNotification(
                pkg=com.sec.android.app.clockpackage user=UserHandle{0}
                id=268439552
                tag=null
                key=0|com.sec.android.app.clockpackage|268439552|null|10178:
                    Notification(
                        channel=notification_channel_firing_alarm_and_timer
                        shortcut=null
                        contentView=null
                        vibrate=null sound=null
                        defaults=0x0
                        flags=0x62
                        color=0x00000000
                        category=alarm
                        groupKey=ALARM_GROUP_KEY
                        vis=PRIVATE
                        semFlags=0x0
                        semPriority=0
                        semMissedCount=0))
        */
    }
})
