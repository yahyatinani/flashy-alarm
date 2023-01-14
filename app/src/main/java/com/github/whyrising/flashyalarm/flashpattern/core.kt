package com.github.whyrising.flashyalarm.flashpattern

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.turnOffLED
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.turnOnLED
import com.github.whyrising.flashyalarm.flashpattern.Ids.blinkConfigDialog
import com.github.whyrising.flashyalarm.flashpattern.Ids.blinkFrequency
import com.github.whyrising.flashyalarm.flashpattern.Ids.blinkFrequencyStr
import com.github.whyrising.flashyalarm.flashpattern.Ids.isTestingFrequency
import com.github.whyrising.flashyalarm.flashpattern.Ids.select_pattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.selected_pattern
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.BLINK
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.SIGNAL
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.STATIC
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.flashyalarm.ui.theme.ConfigColumn
import com.github.whyrising.flashyalarm.ui.theme.ConfigDivider
import com.github.whyrising.flashyalarm.ui.theme.ConfigItem
import com.github.whyrising.flashyalarm.ui.theme.ConfigSection
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.flashyalarm.ui.theme.SectionTitle
import com.github.whyrising.flashyalarm.ui.theme.Yellow700
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.watch
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.composable

fun initFlashPatternsModule(context: Context) {
  regLightFx(context)
  regLightCofx(context)
  regLightPatternsEvents()
  regLightPatternsSubs()
}

@ExperimentalAnimationApi
fun NavGraphBuilder.flashPatterns(animOffSetX: Int) {
  composable(
    route = Ids.patternsRoute.name,
    exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
    popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
  ) {
    FlashlightPatterns()
  }
}

fun stopTesting() {
  dispatch(v(turnOffLED))
  dispatch(v(isTestingFrequency, false))
}

@Composable
fun FlashingSpeedDialog() {
  Dialog(
    onDismissRequest = {
      dispatch(v(blinkConfigDialog, false))
      stopTesting()
    },
  ) {
    Card {
      Column(
        modifier = Modifier
          .padding(
            start = dimensionResource(id = R.dimen.normal_100),
            end = dimensionResource(id = R.dimen.normal_100),
            top = dimensionResource(id = R.dimen.normal_100),
          ),
      ) {
        Text(
          text = "Flashing Speed",
          style = MaterialTheme.typography.labelSmall
        )
        Spacer(
          modifier = Modifier.height(dimensionResource(id = R.dimen.normal_100))
        )
        Text(
          text = watch<String>(v(blinkFrequencyStr)),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onSurface.copy(alpha = .5f)
        )
        Slider(
          value = watch(v(blinkFrequency)),
          valueRange = 100f..1000f,
          steps = 3,
          onValueChange = {
            dispatch(v(blinkFrequency, it))
          },
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(
            onClick = {
              dispatch(v(turnOnLED))
              dispatch(v(isTestingFrequency, true))
            },
            enabled = watch(v(isTestingFrequency)),
            colors = ButtonDefaults.textButtonColors(contentColor = Yellow700)
          ) {
            Text(text = "Test")
          }
          Spacer(
            modifier = Modifier.width(dimensionResource(id = R.dimen.small_100))
          )
          TextButton(
            onClick = {
              stopTesting()
            },
            colors = ButtonDefaults.textButtonColors(
              contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f)
            )
          ) {
            Text(text = "Stop")
          }
        }
      }
    }
  }
}

@Composable
fun FlashlightPatterns() {
  if (watch(v(blinkConfigDialog)))
    FlashingSpeedDialog()

  ConfigColumn {
    SectionTitle("Select")
    ConfigSection {
      ConfigItem(
        trailing = {
          RadioButton(
            selected = watch(v(selected_pattern, STATIC)),
            onClick = { dispatch(v(select_pattern, STATIC)) },
          )
        },
      ) {
        Text(text = "Static")
      }
      ConfigDivider()
      ConfigItem(
        modifier = Modifier
          .clickable(
            enabled = watch(v(selected_pattern, BLINK))
          ) {
            dispatch(v(blinkConfigDialog, true))
          },
        secondaryText = {
          if (watch(v(selected_pattern, BLINK)))
            Text(text = "Tap to customize")
        },
        trailing = {
          RadioButton(
            selected = watch(v(selected_pattern, BLINK)),
            onClick = { dispatch(v(select_pattern, BLINK)) },
          )
        }
      ) {
        Text(text = "Blink")
      }
      ConfigDivider()
      ConfigItem(
        modifier = Modifier
          .clickable(
            enabled = watch(v(selected_pattern, SIGNAL)),
          ) {
            dispatch(v(blinkConfigDialog, true))
          },
        secondaryText = {
          if (watch(v(selected_pattern, SIGNAL)))
            Text(text = "Tap to customize")
        },
        trailing = {
          RadioButton(
            selected = watch(v(selected_pattern, SIGNAL)),
            onClick = { dispatch(v(select_pattern, SIGNAL)) },
          )
        }
      ) {
        Text(text = "Signal")
      }
    }
  }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun FlashlightPatternsPreview() {
  initAppDb()
  initFlashPatternsModule(LocalContext.current)
  FlashyAlarmTheme {
    FlashlightPatterns()
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FlashlightPatternsDarkPreview() {
  initAppDb()
  initFlashPatternsModule(LocalContext.current)
  FlashyAlarmTheme(darkTheme = true) {
    FlashlightPatterns()
  }
}

@Preview(showBackground = true)
@Composable
fun BlinkDialogPreview() {
  initAppDb()
  initFlashPatternsModule(LocalContext.current)
  FlashyAlarmTheme {
    FlashingSpeedDialog()
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BlinkDialogPreviewDarkPreview() {
  initAppDb()
  initFlashPatternsModule(LocalContext.current)
  FlashyAlarmTheme(darkTheme = true) {
    FlashingSpeedDialog()
  }
}
