package com.github.whyrising.flashyalarm.panel.common

import android.content.Context
import android.content.pm.PackageManager
import com.github.whyrising.flashyalarm.panel.common.common.phoneHasTorch
import com.github.whyrising.flashyalarm.panel.flashpattern.LightPatternsDb
import com.github.whyrising.flashyalarm.panel.flashpattern.defaultLightPatternsDb
import com.github.whyrising.recompose.cofx.regCofx

data class AppDb(
  val screenTitle: String,
  val isDisableServiceDialogVisible: Boolean = false,
  val isAlarmListenerRunning: Boolean = false,
  val phoneHasTorch: Boolean = false,
  val lightPatternsDb: LightPatternsDb = defaultLightPatternsDb,
  val isAboutDialogVisible: Boolean = false,
  val isBackstackAvailable: Boolean = false
)

val appDb = AppDb(screenTitle = "")

fun regCofx(context: Context) {
  regCofx(id = phoneHasTorch) { coeffects ->
    coeffects.assoc(
      phoneHasTorch,
      context.packageManager.hasSystemFeature(
        PackageManager.FEATURE_CAMERA_FLASH
      )
    )
  }
}
