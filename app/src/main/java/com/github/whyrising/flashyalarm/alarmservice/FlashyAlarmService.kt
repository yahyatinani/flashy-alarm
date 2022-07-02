package com.github.whyrising.flashyalarm.alarmservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.MainActivity
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmservice.Ids.turnOffLED
import com.github.whyrising.flashyalarm.alarmservice.Ids.turnOnLED
import com.github.whyrising.flashyalarm.base.CHANNEL_ID
import com.github.whyrising.flashyalarm.base.CHANNEL_NAME
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

class FlashyAlarmService : Service() {
  override fun onCreate() {
    super.onCreate()
    Log.i(TAG, "Created.")

    regFx(id = turnOnLED) {
      val cm = application.getSystemService(CAMERA_SERVICE) as CameraManager
      try {
        cm.setTorchMode(cm.cameraIdList[0], true)
      } catch (e: CameraAccessException) {
        throw e
      }
    }

    regFx(id = turnOffLED) {
      val cm = application.getSystemService(CAMERA_SERVICE) as CameraManager
      try {
        cm.setTorchMode(cm.cameraIdList[0], false)
      } catch (e: CameraAccessException) {
        throw e
      }
    }

    regEventFx(id = turnOnLED) { _, _ ->
      m(fx to v(v(turnOnLED, null)))
    }

    regEventFx(id = turnOffLED) { _, _ ->
      m(fx to v(v(turnOffLED, null)))
    }
  }

  @OptIn(ExperimentalAnimationApi::class)
  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.i(TAG, "Started.")
    serviceOn()

    val context = applicationContext
    val intentNotif = Intent(context, MainActivity::class.java)
    intentNotif.flags =
      Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
    val contentIntent = PendingIntent.getActivity(
      context,
      0,
      intentNotif,
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )

    val mNM = NotificationManagerCompat.from(context)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val notificationChannel = NotificationChannel(
        CHANNEL_ID,
        CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
      )
      notificationChannel.description = "TODO: Fix-me!"
      mNM.createNotificationChannel(notificationChannel)
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_status_notification)
      .setTicker(getText(R.string.notif_flashlight_service_content))
      .setWhen(System.currentTimeMillis())
      .setContentTitle(getText(R.string.notif_flashlight_service_title))
      .setContentText(getText(R.string.notif_flashlight_service_content))
      .setContentIntent(contentIntent)
      .build()

    startForeground(R.string.app_name, notification)

    registerReceiver(AlarmBroadcastReceiver(), AlarmBroadcastReceiver.filter)
    return START_STICKY
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun stopService(name: Intent?): Boolean {
    Log.i(TAG, "Stopped.")
    serviceOff()
    return super.stopService(name)
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.i(TAG, "Destroyed.")

    serviceOff()
  }

  companion object {
    val TAG = FlashyAlarmService::class.java.simpleName
    var isRunning = false
      private set

    private fun serviceOff() {
      isRunning = false
    }

    private fun serviceOn() {
      isRunning = true
    }
  }
}
