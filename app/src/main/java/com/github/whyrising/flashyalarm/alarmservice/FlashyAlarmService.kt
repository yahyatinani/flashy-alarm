package com.github.whyrising.flashyalarm.alarmservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
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
import com.github.whyrising.flashyalarm.home.IS_FLASHY_SERVICE_ENABLED
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private fun registerFlashlightFxs(context: Context) {
  regFx(id = turnOnLED) {
    val cm = context.getSystemService(Service.CAMERA_SERVICE) as CameraManager
    try {
      cm.setTorchMode(cm.cameraIdList[0], true)
    } catch (e: CameraAccessException) {
      throw e
    }
  }

  regFx(id = turnOffLED) {
    val cm = context.getSystemService(Service.CAMERA_SERVICE) as CameraManager
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

fun createNotificationChannelIfNeeded(context: Context) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val notificationChannel = NotificationChannel(
      CHANNEL_ID,
      CHANNEL_NAME,
      NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
      description = "TODO: Fix-me!"
    }
    NotificationManagerCompat.from(context).apply {
      createNotificationChannel(notificationChannel)
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
class FlashyAlarmService : Service() {
  private val contentIntent: PendingIntent? by lazy {
    PendingIntent.getActivity(
      application,
      0,
      Intent(application, MainActivity::class.java).apply {
        flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP
      },
      PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
    )
  }

  private val notifBuilder by lazy {
    NotificationCompat.Builder(
      application,
      CHANNEL_ID
    ).apply {
      setSmallIcon(R.drawable.ic_status_notification)
      setTicker(getText(R.string.notif_flashlight_service_content))
      setContentTitle(getText(R.string.notif_flashlight_service_title))
      setContentText(getText(R.string.notif_flashlight_service_content))
      setWhen(System.currentTimeMillis())
      setContentIntent(contentIntent)
    }
  }

  private val alarmBroadcastReceiver by lazy { AlarmBroadcastReceiver() }

  override fun onCreate() {
    super.onCreate()
    Log.i(TAG, "Created.")

    registerFlashlightFxs(context = application)
    createNotificationChannelIfNeeded(application)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.i(TAG, "Started.")
    serviceOn()
    startForeground(R.string.app_name, notifBuilder.build())
    registerReceiver(alarmBroadcastReceiver, AlarmBroadcastReceiver.filter)
    return START_STICKY
  }

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  private fun halt() {
    serviceOff()
    unregisterReceiver(alarmBroadcastReceiver)
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.i(TAG, "Destroyed.")

    halt()
  }

  companion object {
    val TAG = FlashyAlarmService::class.java.simpleName
    var isServiceRunning = false
      private set

    private fun serviceOff() {
      isServiceRunning = false
    }

    private fun serviceOn() {
      isServiceRunning = true
    }
  }
}

class OnBootServiceStarter : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context == null || intent == null) return

    if (intent.action == "android.intent.action.BOOT_COMPLETED") {
      val isEnabled = runBlocking {
        context.dataStore.data.map { preferences ->
          preferences[IS_FLASHY_SERVICE_ENABLED]
        }.first()
      }
      if (isEnabled != null && isEnabled == true)
        context.startService(Intent(context, FlashyAlarmService::class.java))
    }
  }
}
