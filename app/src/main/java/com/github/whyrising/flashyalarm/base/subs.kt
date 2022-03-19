package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.base.Ids.formatScreenTitle
import com.github.whyrising.flashyalarm.base.Ids.screenTitle
import com.github.whyrising.recompose.regSub
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.y.collections.core.v

fun regBaseSubs() {
    regSub<AppDb, String>(
        queryId = screenTitle,
    ) { db, _ ->
        db.screenTitle
    }

    regSub<String, String>(
        queryId = formatScreenTitle,
        signalsFn = { subscribe(v(screenTitle)) }
    ) { title, _ ->
        title.replaceFirstChar { it.uppercase() }
    }
}
