package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.base.Ids.exitApp
import com.github.whyrising.flashyalarm.base.Ids.navigate
import com.github.whyrising.flashyalarm.base.Ids.navigateFx
import com.github.whyrising.flashyalarm.base.Ids.updateScreenTitle
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regBaseEvents() {
  regEventDb<AppDb>(id = updateScreenTitle) { db, (_, title) ->
    db.copy(screenTitle = title as String)
  }

  regEventFx(id = navigate) { _, (_, route) ->
    m(fx to v(v(navigateFx, route)))
  }

  regEventFx(id = exitApp) { _, _ ->
    m(fx to v(v(exitApp, null)))
  }
}
