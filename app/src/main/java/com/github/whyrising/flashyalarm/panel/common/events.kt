package com.github.whyrising.flashyalarm.panel.common

import android.content.Context
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.panel.common.common.exitApp
import com.github.whyrising.flashyalarm.panel.common.common.navigate
import com.github.whyrising.flashyalarm.panel.common.common.navigateFx
import com.github.whyrising.flashyalarm.panel.common.common.setBackstackStatus
import com.github.whyrising.flashyalarm.panel.common.common.updateScreenTitle
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern
import com.github.whyrising.flashyalarm.panel.home.home
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.ids.recompose
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regBaseEvents(c: Context) {
  regEventDb<AppDb>(id = updateScreenTitle) { db, (_, destination) ->
    val title = when (destination) {
      home.homeRoute.name -> c.getString(R.string.home_screen_title)
      flashPattern.patternsRoute.name -> c.getString(
        R.string.patterns_screen_title
      )

      else -> "todo: no title!"
    }
    db.copy(screenTitle = title)
  }

  regEventFx(id = navigate) { _, (_, route) ->
    println("sldkfjsdlj")
    m(fx to v(v(navigateFx, (route as Enum<*>).name)))
  }

  regEventFx(id = exitApp) { _, _ ->
    m(fx to v(v(exitApp, null)))
  }

  regEventDb<AppDb>(setBackstackStatus) { db, (_, flag) ->
    db.copy(isBackstackAvailable = flag as Boolean)
  }

  regEventFx(
    id = common.checkDeviceHasTorch,
    interceptors = v(injectCofx(common.phoneHasTorch))
  ) { cofx, _ ->
    val appDb = cofx[recompose.db] as AppDb
    m(
      recompose.db to appDb.copy(
        phoneHasTorch = cofx[common.phoneHasTorch] as Boolean
      )
    )
  }

  regEventFx(
    id = common.isAlarmListenerRunning,
    interceptors = v(injectCofx(common.isAlarmListenerRunning))
  ) { cofx, _ ->
    val appDb = cofx[recompose.db] as AppDb
    val newDb = appDb.copy(
      isAlarmListenerRunning = cofx[common.isAlarmListenerRunning] as Boolean
    )
    m(recompose.db to newDb)
  }
}
