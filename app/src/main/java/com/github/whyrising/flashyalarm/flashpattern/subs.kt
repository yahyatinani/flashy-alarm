package com.github.whyrising.flashyalarm.flashpattern

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.flashpattern.Ids.blinkFrequency
import com.github.whyrising.flashyalarm.flashpattern.Ids.blinkFrequencyStr
import com.github.whyrising.flashyalarm.flashpattern.Ids.isTestingFrequency
import com.github.whyrising.recompose.regSub

fun regLightPatternsSubs() {
  regSub<AppDb, Boolean>(
    queryId = Ids.selected_pattern
  ) { db, (_, pattern) ->
    db.lightPatternsDb.selectedLightPattern == pattern
  }

  regSub<AppDb, Boolean>(
    queryId = Ids.blinkConfigDialog
  ) { db, _ ->
    db.lightPatternsDb.blinkFrequencyDialog
  }

  regSub<AppDb, Float>(
    queryId = blinkFrequency
  ) { db, _ ->
    db.lightPatternsDb.blinkFrequency.toFloat()
  }

  regSub<AppDb, String>(
    queryId = blinkFrequencyStr
  ) { db, _ ->
    "${db.lightPatternsDb.blinkFrequency} ms"
  }

  regSub<AppDb, String>(
    queryId = blinkFrequencyStr
  ) { db, _ ->
    "${db.lightPatternsDb.blinkFrequency} ms"
  }

  regSub<AppDb, Boolean>(
    queryId = isTestingFrequency
  ) { db, _ ->
    !db.lightPatternsDb.isTestingFrequency
  }
}
