package com.github.whyrising.flashyalarm.panel.flashpattern

import android.content.Context
import com.github.whyrising.flashyalarm.alarmlistenerservice.LightPattern
import com.github.whyrising.flashyalarm.alarmlistenerservice.dataStore
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.blinkFrequency
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.selected_pattern
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

  regCofx(id = blinkFrequency) { coeffects ->
    val frequency = runBlocking {
      context.dataStore.data.map { preferences ->
        preferences[BLINK_FREQUENCY]
      }.first()
    } ?: 300

    coeffects.assoc(blinkFrequency, frequency)
  }
}
