package com.github.whyrising.flashyalarm.alarmservice

import android.content.Context
import android.content.pm.PackageManager.FEATURE_CAMERA_FLASH
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.isFlashHardwareAvailable
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.isFlashServiceRunning
import com.github.whyrising.recompose.cofx.regCofx

fun regCofx(context: Context) {
  regCofx(id = isFlashHardwareAvailable) { coeffects ->
    coeffects.assoc(
      isFlashHardwareAvailable,
      context.packageManager.hasSystemFeature(FEATURE_CAMERA_FLASH)
    )
  }

  regCofx(id = isFlashServiceRunning) { coeffects ->
    coeffects.assoc(isFlashServiceRunning, FlashyAlarmService.isServiceRunning)
  }
}
