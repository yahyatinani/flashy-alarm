package com.github.whyrising.flashyalarm.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.alarmservice.AlarmService
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.isFlashServiceRunning
import com.github.whyrising.flashyalarm.alarmservice.regSubs
import com.github.whyrising.flashyalarm.base.base.isAboutDialogVisible
import com.github.whyrising.flashyalarm.base.base.navigate
import com.github.whyrising.flashyalarm.designsystem.component.ConfigColumn
import com.github.whyrising.flashyalarm.designsystem.component.ConfigDivider
import com.github.whyrising.flashyalarm.designsystem.component.ConfigItem
import com.github.whyrising.flashyalarm.designsystem.component.ConfigSection
import com.github.whyrising.flashyalarm.designsystem.component.Hyperlink
import com.github.whyrising.flashyalarm.designsystem.component.Label
import com.github.whyrising.flashyalarm.designsystem.component.Label2
import com.github.whyrising.flashyalarm.designsystem.component.SectionTitle
import com.github.whyrising.flashyalarm.designsystem.component.SwitchStyled
import com.github.whyrising.flashyalarm.designsystem.theme.FlashyAlarmTheme
import com.github.whyrising.flashyalarm.flashpattern.Ids
import com.github.whyrising.flashyalarm.flashpattern.Ids.previous_frequency_pattern
import com.github.whyrising.flashyalarm.flashpattern.Ids.select_previous_pattern
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.watch
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.composable
import com.github.whyrising.flashyalarm.home.init as initHome

@ExperimentalAnimationApi
fun NavGraphBuilder.home(animOffSetX: Int) {
  composable(
    route = home.homeRoute.name,
    exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
    popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
  ) {
    HomeScreen()
  }
}

@Composable
fun AboutContent() {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.Start
  ) {
    Label(name = stringResource(R.string.by))
    Label2(name = stringResource(R.string.developer))

    Label(name = stringResource(R.string.version_label))
    Label2(name = stringResource(id = R.string.app_version))

    Label(name = stringResource(R.string.src_code_label))
    Hyperlink(url = stringResource(R.string.source_code_link))
  }
}

@Composable
fun AboutDialog() {
  Card {
    AlertDialog(
      title = {
        Text(
          text = stringResource(R.string.about_label),
          style = MaterialTheme.typography.titleSmall
        )
      },
      text = {
        AboutContent()
      },
      onDismissRequest = {
        dispatch(v(isAboutDialogVisible, false))
      },
      confirmButton = {}
    )
  }
}

@Composable
fun HomeScreen() {
  dispatch(v(select_previous_pattern))
  dispatch(v(previous_frequency_pattern))
  dispatch(v(isFlashServiceRunning))

  if (watch(v(isAboutDialogVisible))) {
    AboutDialog()
  }

  ConfigColumn {
    SectionTitle("Service")
    ConfigSection {
      ConfigItem(
        modifier = Modifier
          .padding(bottom = dimensionResource(id = R.dimen.normal_100)),
        secondaryText = {
          Text(text = stringResource(R.string.flashlight_service_switch_desc))
        },
        trailing = {
          SwitchStyled(
            checked = watch(v(isFlashServiceRunning)),
            onCheckedChange = {
              dispatch(v(AlarmService.toggleFlashyAlarmService, it))
            }
          )
        }
      ) {
        Text(text = "Flashlight Service")
      }
    }

    SectionTitle("Configuration")
    ConfigSection {
      ConfigItem(
        modifier = Modifier.clickable {
          dispatch(v(navigate, Ids.patternsRoute))
        },
        secondaryText = { Text(stringResource(R.string.flash_pattern_desc)) }
      ) {
        Text(text = "Flashlight Pattern")
      }
      ConfigDivider()
      ConfigItem(
        modifier = Modifier.clickable {
          dispatch(v(isAboutDialogVisible, true))
        }
      ) {
        Text(text = stringResource(id = R.string.about_label))
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

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
  initAppDb()
  initHome(LocalContext.current)
  regSubs()

  FlashyAlarmTheme {
    AboutDialog()
  }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun AboutDarkPreview() {
  FlashyAlarmTheme(darkTheme = true) {
    AboutDialog()
  }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeDarkPreview() {
  FlashyAlarmTheme(darkTheme = true) {
    HomeScreen()
  }
}
