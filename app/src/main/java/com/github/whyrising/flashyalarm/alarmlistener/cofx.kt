package com.github.whyrising.flashyalarm.alarmlistener

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager.FEATURE_CAMERA_FLASH
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.BuildConfig
import com.github.whyrising.recompose.cofx.regCofx

@ExperimentalAnimationApi
fun regCofx(context: Context) {
    regCofx(id = Ids.isFlashAvailable) { coeffects ->
        coeffects.assoc(
            Ids.isFlashAvailable,
            context.packageManager.hasSystemFeature(FEATURE_CAMERA_FLASH)
        )
    }

    regCofx(id = Ids.isNotifAccessEnabled) { coeffects ->
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
        coeffects.assoc(Ids.isNotifAccessEnabled, b)
    }
}
