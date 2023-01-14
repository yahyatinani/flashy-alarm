package com.github.whyrising.flashyalarm.panel.flashpattern

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.whyrising.flashyalarm.alarmservice.dataStore
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.saveBlinkFrequency
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.savePattern
import com.github.whyrising.recompose.regFx
import kotlinx.coroutines.runBlocking

val LIGHT_PATTERN = stringPreferencesKey("lightPattern")
val BLINK_FREQUENCY = longPreferencesKey("blinking_frequency")

fun regLightFx(context: Context) {
  regFx(id = savePattern) { pattern ->
    runBlocking {
      context.dataStore.edit { settings ->
        settings[LIGHT_PATTERN] = (pattern as LightPattern).name
      }
    }
  }

  regFx(id = saveBlinkFrequency) { f ->
    runBlocking {
      context.dataStore.edit { settings ->
        settings[BLINK_FREQUENCY] = (f as Long)
      }
    }
  }
}
