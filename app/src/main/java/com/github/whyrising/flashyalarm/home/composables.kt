package com.github.whyrising.flashyalarm.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmservice.Ids
import com.github.whyrising.flashyalarm.alarmservice.Ids.isFlashServiceRunning
import com.github.whyrising.flashyalarm.alarmservice.regSubs
import com.github.whyrising.flashyalarm.base.Ids.navigate
import com.github.whyrising.flashyalarm.base.Ids.updateScreenTitle
import com.github.whyrising.flashyalarm.flashpattern.Ids.previous_frequency_pattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.select_previous_pattern
import com.github.whyrising.flashyalarm.flashpattern.patternsRoute
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.flashyalarm.ui.theme.ConfigColumn
import com.github.whyrising.flashyalarm.ui.theme.ConfigDivider
import com.github.whyrising.flashyalarm.ui.theme.ConfigItem
import com.github.whyrising.flashyalarm.ui.theme.ConfigSection
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.flashyalarm.ui.theme.SectionTitle
import com.github.whyrising.flashyalarm.ui.theme.SwitchStyled
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.composable
import com.github.whyrising.flashyalarm.home.init as initHome

const val homeRoute = "/home"

@ExperimentalAnimationApi
fun NavGraphBuilder.home(animOffSetX: Int) {
  composable(
    route = homeRoute,
    exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
    popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
  ) {
    HomeScreen()
  }
}

@Composable
fun HomeScreen() {
  dispatch(v(updateScreenTitle, stringResource(R.string.home_screen_title)))
  dispatch(v(select_previous_pattern))
  dispatch(v(previous_frequency_pattern))
  dispatch(v(isFlashServiceRunning))

  ConfigColumn {
    SectionTitle("Service")
    ConfigSection {
      ConfigItem(
        modifier = Modifier.padding(bottom = 8.dp),
        secondaryText = {
          Text(text = stringResource(R.string.flashlight_service_switch_desc))
        },
        trailing = {
          SwitchStyled(
            checked = subscribe<Boolean>(v(isFlashServiceRunning)).w(),
            onCheckedChange = {
              dispatch(v(Ids.toggleFlashyAlarmService, it))
            }
          )
        },
      ) {
        Text(text = "Flashlight Service")
      }
    }

    SectionTitle("Configuration")
    ConfigSection {
      ConfigItem(
        modifier = Modifier.clickable {
          dispatch(v(navigate, patternsRoute))
        },
        secondaryText = { Text(stringResource(R.string.flash_pattern_desc)) },
      ) {
        Text(text = "Flashlight Pattern")
      }
      ConfigDivider()
      ConfigItem(
        modifier = Modifier.clickable {
          // TODO: show About dialog.
        }
      ) {
        Text(text = "About")
      }
    }
  }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun HomePreview() {
  initAppDb()
  initHome(LocalContext.current)
  regSubs()

  FlashyAlarmTheme {
    HomeScreen()
  }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeDarkPreview() {
  FlashyAlarmTheme(darkTheme = true) {
    HomeScreen()
  }
}
