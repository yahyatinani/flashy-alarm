package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.base.Ids.exitApp
import com.github.whyrising.recompose.regFx
import kotlin.system.exitProcess

fun regBaseFx() {
    regFx(id = exitApp) { value ->
        exitProcess(value as Int)
    }
}
