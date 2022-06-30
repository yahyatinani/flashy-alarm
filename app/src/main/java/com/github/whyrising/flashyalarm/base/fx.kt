package com.github.whyrising.flashyalarm.base

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.base.Ids.exitApp
import com.github.whyrising.flashyalarm.base.Ids.pushNotification
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.core.collections.PersistentArrayMap
import kotlin.system.exitProcess

const val CHANNEL_ID = "flashlight_service_notification_channel"
const val CHANNEL_NAME = "Flashy Alarm Notification Channel"

fun <K> regBaseFx(context: Context, clazz: Class<K>) {
  regFx(id = exitApp) { value ->
    exitProcess(value as Int)
  }

  regFx(id = pushNotification) { notifData ->
    notifData as PersistentArrayMap<*, *>
    val content = notifData["content"]
    val title = notifData["title"]
    val notificationId = notifData["id"]

    val intent = Intent(context, clazz)
    intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP
    val contentIntent = PendingIntent.getActivity(
      context,
      0,
      intent,
      FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT,
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
      .setTicker(content as CharSequence?)
      .setWhen(System.currentTimeMillis())
      .setContentTitle(title as CharSequence?)
      .setContentText(content)
      .setContentIntent(contentIntent)
      .build()

    mNM.notify(notificationId as Int, notification)
  }
}
