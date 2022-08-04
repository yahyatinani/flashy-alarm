package com.github.whyrising.flashyalarm.flashpattern

import android.content.Context
import com.github.whyrising.flashyalarm.alarmservice.dataStore
import com.github.whyrising.flashyalarm.flashpattern.patterns.blinkFrequency
import com.github.whyrising.flashyalarm.flashpattern.patterns.selected_pattern
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
