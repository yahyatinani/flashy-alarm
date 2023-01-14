package com.github.whyrising.flashyalarm.panel.common

import com.github.whyrising.flashyalarm.panel.common.common.formatScreenTitle
import com.github.whyrising.flashyalarm.panel.common.common.screenTitle
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

  regSub<AppDb, Boolean>(common.isBackstackAvailable) { db, _ ->
    db.isBackstackAvailable
  }
}
