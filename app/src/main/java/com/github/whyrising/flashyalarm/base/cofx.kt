package com.github.whyrising.flashyalarm.base

import android.content.Context
import android.content.pm.PackageManager.FEATURE_CAMERA_FLASH
import com.github.whyrising.flashyalarm.Ids.is_flash_available
import com.github.whyrising.recompose.cofx.regCofx

fun regBaseCofx(context: Context) {
    regCofx(id = is_flash_available) { coeffects ->
        coeffects.assoc(
            is_flash_available,
            context.packageManager.hasSystemFeature(FEATURE_CAMERA_FLASH)
        )
    }
}
