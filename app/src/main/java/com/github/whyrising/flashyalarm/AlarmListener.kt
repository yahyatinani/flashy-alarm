package com.github.whyrising.flashyalarm

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.activity.ComponentActivity
import com.github.whyrising.flashyalarm.Ids.flash_on
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

class AlarmListener : NotificationListenerService() {
    override fun onCreate() {
        super.onCreate()

        regEventFx(id = flash_on) { _, (_, context, stBarNotif) ->
            when ((stBarNotif as StatusBarNotification).notification.category) {
                "alarm" -> m(flash_on to context)
                else -> m()
            }
        }
        regFx(flash_on) { context ->
            val cameraManager = (context as Context).getSystemService(
                ComponentActivity.CAMERA_SERVICE
            ) as CameraManager
            try {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
            } catch (e: CameraAccessException) {
                throw e
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        // TODO: Change API in re-compose, make Event: IPersistentVector<Any?>
        //  in 0.0.7
        if (sbn == null) return

        dispatch(v<Any>(flash_on, application, sbn))
    }
}
