package com.github.whyrising.flashyalarm.notificationdialog

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.AlarmListener
import com.github.whyrising.flashyalarm.BuildConfig
import com.github.whyrising.flashyalarm.Ids.is_notif_access_enabled
import com.github.whyrising.recompose.cofx.regCofx

fun regNotifDialogCofx(context: Context) {
    regCofx(id = is_notif_access_enabled) { coeffects ->
        val b = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1 -> {
                val name = ComponentName(context, AlarmListener::class.java)
                val notificationManager = context.getSystemService(
                    ComponentActivity.NOTIFICATION_SERVICE
                ) as NotificationManager
                notificationManager.isNotificationListenerAccessGranted(name)
            }
            else -> {
                val packages = NotificationManagerCompat
                    .getEnabledListenerPackages(context)
                Log.i("enabledListener", "$packages")
                packages.contains(BuildConfig.APPLICATION_ID)
            }
        }
        coeffects.assoc(is_notif_access_enabled, b)
    }
}
