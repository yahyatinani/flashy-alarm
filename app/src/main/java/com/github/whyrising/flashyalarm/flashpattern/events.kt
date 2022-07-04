package com.github.whyrising.flashyalarm.flashpattern

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.flashpattern.Ids.savePattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.select_pattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.select_previous_pattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.selected_pattern
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema.db
import com.github.whyrising.y.core.get
import com.github.whyrising.y.core.m
import com.github.whyrising.y.core.v

fun regLightPatternsEvents() {
  regEventFx(id = select_pattern) { cofx, (_, pattern) ->
    val appDb = cofx[db] as AppDb
    m(
      db to appDb.copy(selectedLightPattern = pattern as LightPattern),
      fx to v(v(savePattern, pattern))
    )
  }

  regEventFx(
    id = select_previous_pattern,
    interceptors = v(injectCofx(selected_pattern))
  ) { cofx, _ ->
    val appDb = cofx[db] as AppDb
    val lightPattern = cofx[selected_pattern] as LightPattern
    m(
      db to appDb.copy(selectedLightPattern = lightPattern),
      fx to v(v(savePattern, lightPattern))
    )
  }
}
