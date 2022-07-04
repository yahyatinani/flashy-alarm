package com.github.whyrising.flashyalarm.flashpattern

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.whyrising.flashyalarm.alarmservice.dataStore
import com.github.whyrising.flashyalarm.flashpattern.Ids.savePattern
import com.github.whyrising.recompose.regFx
import kotlinx.coroutines.runBlocking

val LIGHT_PATTERN = stringPreferencesKey("lightPattern")

fun regLightFx(context: Context) {
  regFx(id = savePattern) { pattern ->
    runBlocking {
      context.dataStore.edit { settings ->
        settings[LIGHT_PATTERN] = (pattern as LightPattern).name
      }
    }
  }
}
