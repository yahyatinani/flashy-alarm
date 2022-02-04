package com.github.whyrising.flashyalarm.global

import com.github.whyrising.flashyalarm.Ids
import com.github.whyrising.recompose.regFx
import kotlin.system.exitProcess

fun regGlobalFx() {
    regFx(Ids.exit_app) { value ->
        exitProcess(value as Int)
    }
}
