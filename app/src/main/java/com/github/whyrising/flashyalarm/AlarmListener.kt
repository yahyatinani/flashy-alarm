package com.github.whyrising.flashyalarm

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getService
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.Ids.flash_on
import com.github.whyrising.flashyalarm.Ids.stop_alarm_listener
import com.github.whyrising.flashyalarm.global.DbSchema
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.schemas.Schema
import com.github.whyrising.y.collections.core.get
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

class AlarmListener : NotificationListenerService() {
    private fun createOngoingNotification(): Notification {
        // TODO: make notification sticky
        val nm = NotificationManagerCompat.from(application)
        val channelId = "NOTIFICATION_CHANNEL_ID"
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                channelId,
                "channelName",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        channel.description = "TODO: Fix-me!"
        nm.createNotificationChannel(channel)

        return NotificationCompat
            .Builder(this, channelId)
            .setOngoing(true) // TODO: control this by the service status
            .setContentTitle(getString(R.string.notif_ongo_title))
            .setContentText(getString(R.string.notif_ongo_content))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            //                .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.app_name))
            .addAction(
                R.drawable.ic_anim_splash,
                "Turn off",
                getService(
                    application,
                    955421,
                    Intent(),
                    FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
                )
            )
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "onCreate() ${hashCode()}")

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

        regEventFx(id = flash_on) { _, (_, context, stBarNotif) ->
            // TODO: Fix, turn with alarm only, not timer
            val statusBarNotification = stBarNotif as StatusBarNotification
            when (statusBarNotification.notification.category) {
                "alarm" -> m(flash_on to context)
                else -> m()
            }
        }

        regFx(id = stop_alarm_listener) {
            stopForeground(true)
            (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .clearApplicationUserData()
            Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show()
        }

        regEventFx(id = stop_alarm_listener) { cofx, _ ->
            val appDb = cofx[Schema.db] as DbSchema
            m(
                Schema.db to appDb.copy(
                    isAlarmListenerRunning = false,
                    isNotifAccessEnabled = false,
                ),
                stop_alarm_listener to true
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(TAG, "onDestroy() ${hashCode()}")
        dispatch(v(stop_alarm_listener))
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        Log.i(TAG, "onStartCommand() ${hashCode()}")
        startForeground(R.string.app_name, createOngoingNotification())
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show()

        return START_STICKY
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.i(TAG, "$sbn")
        // TODO: Change API in re-compose, make Event: IPersistentVector<Any?>
        //  in 0.0.7
        if (sbn == null) return

        dispatch(v<Any>(flash_on, application, sbn))
    }

    companion object {
        private val TAG = this::class.java.simpleName
    }
}
