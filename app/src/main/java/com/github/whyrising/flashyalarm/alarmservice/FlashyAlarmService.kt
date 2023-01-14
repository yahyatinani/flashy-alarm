package com.github.whyrising.flashyalarm.alarmservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.MainActivity
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.base.CHANNEL_ID
import com.github.whyrising.flashyalarm.base.CHANNEL_NAME

@OptIn(ExperimentalAnimationApi::class)
class FlashyAlarmService : Service() {
  private val contentIntent: PendingIntent? by lazy {
    PendingIntent.getActivity(
      application,
      0,
      Intent(application, MainActivity::class.java).apply {
        flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP
      },
      FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT,
    )
  }

  private val notifBuilder by lazy {
    NotificationCompat.Builder(
      application,
      CHANNEL_ID
    ).apply {
      setOngoing(true)
      setSmallIcon(R.drawable.ic_status_notification)
      setTicker(getText(R.string.notif_flashlight_service_content))
      setContentTitle(getText(R.string.notif_flashlight_service_title))
      setContentText(getText(R.string.notif_flashlight_service_content))
      setWhen(System.currentTimeMillis())
      setContentIntent(contentIntent)
      foregroundServiceBehavior = FOREGROUND_SERVICE_IMMEDIATE
      val intent = Intent(application, OnBootServiceStarter::class.java).apply {
        putExtra(DISABLE_SERVICE, true)
      }
      val pendingIntent = PendingIntent.getBroadcast(
        application,
        0,
        intent,
        FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT
      )
      addAction(R.drawable.ic_status_notification, "Disable", pendingIntent)
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
  }
}
