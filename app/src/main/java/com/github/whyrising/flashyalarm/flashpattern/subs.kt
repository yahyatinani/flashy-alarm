package com.github.whyrising.flashyalarm.flashpattern

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.regSub

fun regLightPatternsSubs() {
  regSub<AppDb, Boolean>(
    queryId = Ids.selected_pattern,
  ) { db, (_, pattern) ->
    db.selectedLightPattern == pattern
  }
}
