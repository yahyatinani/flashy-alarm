package com.github.whyrising.flashyalarm.flashpattern

import android.content.Context
import com.github.whyrising.flashyalarm.alarmservice.dataStore
import com.github.whyrising.flashyalarm.flashpattern.Ids.selected_pattern
import com.github.whyrising.recompose.cofx.regCofx
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun regLightCofx(context: Context) {
  regCofx(id = selected_pattern) { coeffects ->
    val name = runBlocking {
      context.dataStore.data.map { preferences ->
        preferences[LIGHT_PATTERN]
      }.first()
    } ?: LightPattern.STATIC.name

    coeffects.assoc(selected_pattern, LightPattern.patternBy(name))
  }
}
