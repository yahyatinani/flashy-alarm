package com.github.whyrising.app.home

import android.os.Build
import com.github.whyrising.app.Keys.sdk_version
import com.github.whyrising.recompose.cofx.regCofx

data class DbHomeSchema(
    val isAboutBtnEnabled: Boolean,
    val sdkVersion: Int,
    val count: Int
)

val defaultDbHomeSchema = DbHomeSchema(
    isAboutBtnEnabled = true,
    sdkVersion = -1,
    count = 0
)

// -- cofx Registrations -------------------------------------------------------

fun regHomeCofx() {
    regCofx(id = sdk_version) { coeffects ->
        coeffects.assoc(sdk_version, Build.VERSION.SDK_INT)
    }
}
