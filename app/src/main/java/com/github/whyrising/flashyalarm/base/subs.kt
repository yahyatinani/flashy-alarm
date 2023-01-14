package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.base.base.formatScreenTitle
import com.github.whyrising.flashyalarm.base.base.screenTitle
import com.github.whyrising.recompose.regSub
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.y.core.v

fun regBaseSubs() {
  regSub<String, String>(
    queryId = formatScreenTitle,
    initialValue = "",
    signalsFn = { subscribe(v(screenTitle)) }
  ) { title, _, _ ->
    title.replaceFirstChar { it.uppercase() }
  }

  regSub<AppDb, String>(
    queryId = screenTitle
  ) { db, _ ->
    db.screenTitle
  }

  regSub<AppDb, Boolean>(base.isBackstackAvailable) { db, _ ->
    db.isBackstackAvailable
  }
}
