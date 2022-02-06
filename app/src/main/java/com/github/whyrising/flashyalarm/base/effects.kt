package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.Ids
import com.github.whyrising.recompose.regFx
import kotlin.system.exitProcess

fun regBaseFx() {
    regFx(Ids.exit_app) { value ->
        exitProcess(value as Int)
    }
}
