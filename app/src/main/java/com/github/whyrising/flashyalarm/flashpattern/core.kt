package com.github.whyrising.flashyalarm.flashpattern

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.base.Ids
import com.github.whyrising.flashyalarm.flashpattern.Ids.select_pattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.select_previous_pattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.selected_pattern
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.BLINK
import com.github.whyrising.flashyalarm.flashpattern.LightPattern.STATIC
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.composable

const val patternsRoute = "flashlight_patterns"

@ExperimentalAnimationApi
fun NavGraphBuilder.flashPatterns(animOffSetX: Int) {
  composable(
    route = patternsRoute,
    exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
    popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
  ) {
    FlashlightPatterns()
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FlashlightPatterns() {
  regLightFx(LocalContext.current)
  regLightCofx(LocalContext.current)
  regLightPatternsEvents()
  regLightPatternsSubs()

  LaunchedEffect(true) {
    dispatch(v(Ids.updateScreenTitle, "Flash Patterns"))
    dispatch(v(select_previous_pattern))
  }

  Surface {
    Column(modifier = Modifier.fillMaxSize()) {
      ListItem(
        overlineText = { Text(text = "Select") },
        trailing = {
          RadioButton(
            selected = subscribe<Boolean>(v(selected_pattern, STATIC)).w(),
            onClick = { dispatch(v(select_pattern, STATIC)) },
          )
        }
      ) {
        Text(text = "Static")
      }
      ListItem(
        modifier = Modifier.clickable {
          // TODO:
        },
        secondaryText = {
          Text(text = "Tap to customize")
        },
        trailing = {
          RadioButton(
            selected = subscribe<Boolean>(v(selected_pattern, BLINK)).w(),
            onClick = { dispatch(v(select_pattern, BLINK)) },
          )
        }
      ) {
        Text(text = "Blink")
      }
    }
  }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun FlashlightPatternsPreview() {
  initAppDb()
  FlashyAlarmTheme {
    FlashlightPatterns()
  }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FlashlightPatternsDarkPreview() {
  initAppDb()
  FlashyAlarmTheme(darkTheme = true) {
    FlashlightPatterns()
  }
}
