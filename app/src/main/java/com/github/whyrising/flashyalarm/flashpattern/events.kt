package com.github.whyrising.flashyalarm.flashpattern

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.flashpattern.patterns.blinkConfigDialog
import com.github.whyrising.flashyalarm.flashpattern.patterns.blinkFrequency
import com.github.whyrising.flashyalarm.flashpattern.patterns.isTestingFrequency
import com.github.whyrising.flashyalarm.flashpattern.patterns.saveBlinkFrequency
import com.github.whyrising.flashyalarm.flashpattern.patterns.savePattern
import com.github.whyrising.flashyalarm.flashpattern.patterns.select_pattern
import com.github.whyrising.flashyalarm.flashpattern.patterns.select_previous_pattern
import com.github.whyrising.flashyalarm.flashpattern.patterns.selected_pattern
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.ids.recompose.db
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regLightPatternsEvents() {
  regEventFx(id = select_pattern) { cofx, (_, pattern) ->
    val appDb = cofx[db] as AppDb
    m(
      db to appDb.copy(
        lightPatternsDb = appDb.lightPatternsDb.copy(
          selectedLightPattern = pattern as LightPattern
        )
      ),
      fx to v(v(savePattern, pattern))
    )
  }

  regEventFx(
    id = select_previous_pattern,
    interceptors = v(injectCofx(selected_pattern))
  ) { cofx, _ ->
    val appDb = cofx[db] as AppDb
    m(
      db to appDb.copy(
        lightPatternsDb = appDb.lightPatternsDb.copy(
          selectedLightPattern = cofx[selected_pattern] as LightPattern
        )
      )
    )
  }

  regEventDb<AppDb>(blinkConfigDialog) { db, (_, flag) ->
    db.copy(
      lightPatternsDb = db.lightPatternsDb.copy(
        blinkFrequencyDialog = flag as Boolean
      )
    )
  }

  regEventFx(id = blinkConfigDialog) { cofx, (_, flag) ->
    val appDb = cofx[db] as AppDb
    val newDb = appDb.copy(
      lightPatternsDb = appDb.lightPatternsDb.copy(
        blinkFrequencyDialog = flag as Boolean
      )
    )
    m(
      db to newDb,
      fx to v(
        if (!flag) v(saveBlinkFrequency, newDb.lightPatternsDb.blinkFrequency)
        else null
      )
    )
  }

  regEventDb<AppDb>(blinkFrequency) { db, (_, f) ->
    db.copy(
      lightPatternsDb = db.lightPatternsDb.copy(
        blinkFrequency = (f as Float).toLong()
      )
    )
  }

  regEventDb<AppDb>(isTestingFrequency) { db, (_, f) ->
    db.copy(
      lightPatternsDb = db.lightPatternsDb.copy(
        isTestingFrequency = f as Boolean
      )
    )
  }

  regEventFx(
    id = patterns.previous_frequency_pattern,
    interceptors = v(injectCofx(blinkFrequency))
  ) { cofx, _ ->
    val appDb = cofx[db] as AppDb
    m(
      db to appDb.copy(
        lightPatternsDb = appDb.lightPatternsDb.copy(
          blinkFrequency = cofx[blinkFrequency] as Long
        )
      )
    )
  }
}
