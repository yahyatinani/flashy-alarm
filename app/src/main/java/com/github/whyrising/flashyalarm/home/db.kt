package com.github.whyrising.flashyalarm.home

import android.os.Build
import com.github.whyrising.flashyalarm.Keys.sdk_version
import com.github.whyrising.recompose.cofx.regCofx

data class DbHomeSchema(
    val sdkVersion: Int,
    val count: Int
)

val defaultDbHomeSchema = DbHomeSchema(
    sdkVersion = -1,
    count = 0
)

// -- cofx Registrations -------------------------------------------------------

fun regHomeCofx() {
    regCofx(id = sdk_version) { coeffects ->
        coeffects.assoc(sdk_version, Build.VERSION.SDK_INT)
    }
}
