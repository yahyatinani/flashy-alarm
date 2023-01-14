package com.github.whyrising.flashyalarm.base

import android.content.Context
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.base.base.exitApp
import com.github.whyrising.flashyalarm.base.base.navigate
import com.github.whyrising.flashyalarm.base.base.navigateFx
import com.github.whyrising.flashyalarm.base.base.updateScreenTitle
import com.github.whyrising.flashyalarm.flashpattern.Ids
import com.github.whyrising.flashyalarm.home.home
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regBaseEvents(c: Context) {
  regEventDb<AppDb>(id = updateScreenTitle) { db, (_, destination) ->
    val title = when (destination) {
      home.homeRoute.name -> c.getString(R.string.home_screen_title)
      Ids.patternsRoute.name -> c.getString(R.string.patterns_screen_title)
      else -> "todo: no title!"
    }
    db.copy(screenTitle = title)
  }

  regEventFx(id = navigate) { _, (_, route) ->
    m(fx to v(v(navigateFx, (route as Enum<*>).name)))
  }

  regEventFx(id = exitApp) { _, _ ->
    m(fx to v(v(exitApp, null)))
  }

  regEventDb<AppDb>(base.setBackstackStatus) { db, (_, flag) ->
    db.copy(isBackstackAvailable = flag as Boolean)
  }
}
