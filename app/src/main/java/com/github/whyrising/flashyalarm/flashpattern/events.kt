package com.github.whyrising.flashyalarm.flashpattern

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.regEventDb

fun regLightPatternsEvents() {
  regEventDb<AppDb>(id = Ids.select_pattern) { db, (_, pattern) ->
    db.copy(selectedLightPattern = pattern as LightPattern)
  }
}
