package com.github.whyrising.flashyalarm.panel.flashpattern

import com.github.whyrising.flashyalarm.panel.common.AppDb
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.blinkFrequency
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.blinkFrequencyStr
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.isTestingFrequency
import com.github.whyrising.recompose.regSub

fun regLightPatternsSubs() {
  regSub<AppDb, Boolean>(
    queryId = flashPattern.selected_pattern
  ) { db, (_, pattern) ->
    db.lightPatternsDb.selectedLightPattern == pattern
  }

  regSub<AppDb, Boolean>(
    queryId = flashPattern.blinkConfigDialog
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
