package com.github.whyrising.flashyalarm.alarmlistener

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.activity.ComponentActivity
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmlistener.Ids.flashOn
import com.github.whyrising.flashyalarm.alarmlistener.Ids.stopAlarmListener
import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.base.Ids.pushNotification
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.fx.Effects
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.schemas.Schema
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

/**
 * Initialize [AlarmListener] module.
 *
 * @param context passed by a lifecycle aware component.
 */
fun init(context: Context) {
  regFx(context = context)
  regCofx(context = context)
  regEvents()
  regSubs()
}

const val SAMSUNG_ALARM_PKG = "com.sec.android.app.clockpackage"

internal fun flashlightEffect(
  pkgName: String,
  group: String?,
  category: String?
): Effects {
  return when (category) {
    "alarm" -> {
      if (pkgName == SAMSUNG_ALARM_PKG)
        if (group != "ALARM_GROUP_KEY")
          return m()
      m(flashOn to true)
    }
    else -> m()
  }
}

class AlarmListener : NotificationListenerService() {
  internal val TAG: String = this::class.java.simpleName

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

    regEventFx(id = flashOn) { _, (_, sbn) ->
      sbn as StatusBarNotification
      val pkgName = sbn.packageName
      val notification = sbn.notification
      val category = notification.category
      val group = notification.group
      flashlightEffect(
        pkgName = pkgName,
        group = group,
        category = category
      )
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

    val notifId = R.string.app_name
    val content = getText(R.string.notif_flashlight_service_content)
    val title = getText(R.string.notif_flashlight_service_title)
    dispatch(v(pushNotification, notifId, title, content))
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
}
