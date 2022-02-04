package com.github.whyrising.flashyalarm

import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.github.whyrising.y.concurrency.atom

class AlarmListener : NotificationListenerService() {
    private val flashLightStatus = atom(false)

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun flash(context: Context) {
        val cameraManager = context.getSystemService(
            ComponentActivity.CAMERA_SERVICE
        ) as CameraManager
        try {
            cameraManager.setTorchMode(
                cameraManager.cameraIdList[0],
                flashLightStatus.swap { true }
            )
        } catch (e: CameraAccessException) {
            throw e
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if (sbn == null) return
        Log.i("AlarmListener", "$sbn")

        if (sbn.notification.category == "alarm") {
            flash(application)
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}
