package com.github.whyrising.flashyalarm.alarmlistener

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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.MainActivity
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmlistener.Ids.flashOn
import com.github.whyrising.flashyalarm.alarmlistener.Ids.stopAlarmListener
import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.fx.Effects
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.schemas.Schema
import com.github.whyrising.y.collections.core.get
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

/**
 * Initialize [AlarmListener] module.
 *
 * @param context passed by a lifecycle aware component.
 */
@ExperimentalAnimationApi
fun init(context: Context) {
    regFx(context = context)
    regCofx(context = context)
    regEvents()
    regSubs()
}

internal fun flashlightOn(
    category: String?,
    groupKey: String?
): Effects = when {
    category != "alarm" -> m()
    groupKey != "ALARM_GROUP_KEY" -> m()
    else -> m(flashOn to true)
}

@ExperimentalAnimationApi
class AlarmListener : NotificationListenerService() {
    internal val TAG: String = this::class.java.simpleName

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
            .setSmallIcon(R.drawable.ic_status_notification)
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

        regFx(id = flashOn) {
            val cameraManager = application.getSystemService(
                ComponentActivity.CAMERA_SERVICE
            ) as CameraManager
            try {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
            } catch (e: CameraAccessException) {
                throw e
            }
        }

        regEventFx(id = flashOn) { _, (_, statBarNotif) ->
            val n = (statBarNotif as StatusBarNotification).notification
            flashlightOn(n.category, n.group)
        }

        regFx(id = stopAlarmListener) {
            (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
                .clearApplicationUserData()
        }

        regEventFx(id = stopAlarmListener) { cofx, _ ->
            val appDb = cofx[Schema.db] as AppDb
            m(
                Schema.db to appDb.copy(
                    alarmListenerDb = appDb.alarmListenerDb.copy(
                        isNotifAccessEnabled = false
                    )
                ),
                stopAlarmListener to true
            )
        }

        showNotification()
    }

    override fun onDestroy() {
        super.onDestroy()

        dispatch(v(stopAlarmListener))
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int = START_STICKY

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        Log.i(TAG, "$sbn")
        // TODO: Change API in re-compose, make Event: IPersistentVector<Any?>
        //  in 0.0.7
        if (sbn == null) return

        dispatch(v(flashOn, sbn))
    }

    companion object {
        private const val NOTIFICATION_ID = R.string.app_name
        const val CHANNEL_ID = "flashlight_service_notification_channel"
        private const val CHANNEL_NAME = "Flashy Alarm Notification Channel"
    }
}
