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


class AlarmListener : NotificationListenerService() {
//    var flashLightStatus: Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    private fun flash(context: Context) {
        val cameraManager = context.getSystemService(
            ComponentActivity.CAMERA_SERVICE
        ) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        try {
            cameraManager.setTorchMode(cameraId, true)
        } catch (e: CameraAccessException) {
        }
//        else {
//            try {
//                cameraManager.setTorchMode(cameraId, false)
//                flashLightStatus = false
//            } catch (e: CameraAccessException) {
//            }
//        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if (sbn == null) return
        Log.i("AlarmListener", "$sbn")

        if (sbn.notification.category == "alarm") {
            flash(application)
        }
//        if (sbn != null && sbn.groupKey == "ALARM_GROUP_KEY") {
//            Log.i("AlarmListener", "$sbn")
//        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

//        Log.i("AlarmListener", "$sbn")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
    }
}
