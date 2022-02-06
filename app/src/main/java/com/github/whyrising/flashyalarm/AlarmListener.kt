package com.github.whyrising.flashyalarm

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
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
    private fun showNotification() {
        val mNM = NotificationManagerCompat.from(application)

        val text = getText(R.string.notif_ongo_content)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "TODO: Fix-me!"
            mNM.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setTicker(text)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(getText(R.string.notif_flashlight_service_title))
            .setContentText(text)
            .setContentIntent(contentIntent)
            .build()

        mNM.notify(NOTIFICATION_ID, notification)
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
            (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .clearApplicationUserData()
        }

        regEventFx(id = stop_alarm_listener) { cofx, _ ->
            val appDb = cofx[Schema.db] as DbSchema
            m(
                Schema.db to appDb.copy(
                    isNotifAccessEnabled = false,
                ),
                stop_alarm_listener to true
            )
        }

        showNotification()
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
        private const val NOTIFICATION_ID = R.string.app_name
        private const val CHANNEL_ID = "flashlight_service_notification_channel"
        private const val CHANNEL_NAME = "Flashy Alarm Notification Channel"
    }
}
