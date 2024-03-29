package com.github.whyrising.flashyalarm.panel.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.designsystem.component.AlertDialogBodyText
import com.github.whyrising.flashyalarm.designsystem.component.AlertDialogBodyTitle
import com.github.whyrising.flashyalarm.designsystem.component.FaColumn
import com.github.whyrising.flashyalarm.designsystem.component.FaDivider
import com.github.whyrising.flashyalarm.designsystem.component.FaListItem
import com.github.whyrising.flashyalarm.designsystem.component.FaListItemTitle
import com.github.whyrising.flashyalarm.designsystem.component.Hyperlink
import com.github.whyrising.flashyalarm.designsystem.component.SectionTitle
import com.github.whyrising.flashyalarm.designsystem.component.SwitchStyled
import com.github.whyrising.flashyalarm.designsystem.theme.FlashyAlarmTheme
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.panel.common.common
import com.github.whyrising.flashyalarm.panel.common.common.isAboutDialogVisible
import com.github.whyrising.flashyalarm.panel.common.common.navigate
import com.github.whyrising.flashyalarm.panel.common.regBaseSubs
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.previous_frequency_pattern
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPattern.select_previous_pattern
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.watch
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.composable

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
fun AboutDialog() {
  AlertDialog(
    title = {
      Text(
        text = stringResource(R.string.about_label),
        style = MaterialTheme.typography.titleLarge
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
      ) {
        AlertDialogBodyTitle(name = stringResource(R.string.by))
        AlertDialogBodyText(name = stringResource(R.string.developer))

        AlertDialogBodyTitle(name = stringResource(R.string.version_label))
        AlertDialogBodyText(name = stringResource(id = R.string.app_version))

        AlertDialogBodyTitle(name = stringResource(R.string.src_code_label))
        Hyperlink(url = stringResource(R.string.source_code_link))
      }
    },
    onDismissRequest = {
      dispatch(v(isAboutDialogVisible, false))
    },
    confirmButton = {}
  )
}

@Composable
fun HomeScreen() {
  dispatch(v(select_previous_pattern))
  dispatch(v(previous_frequency_pattern))
  dispatch(v(common.isAlarmListenerRunning))

  if (watch(v(isAboutDialogVisible))) {
    AboutDialog()
  }

  FaColumn {
    SectionTitle("Service")
    FaListItem(
      modifier = Modifier
        .padding(bottom = dimensionResource(id = R.dimen.normal_100)),
      secondaryText = {
        Text(text = stringResource(R.string.flashlight_service_switch_desc))
      },
      trailing = {
        SwitchStyled(
          checked = watch(v(common.isAlarmListenerRunning)),
          onCheckedChange = {
            dispatch(v(home.toggleFlashyAlarmService, it))
          }
        )
      }
    ) {
      FaListItemTitle(text = "Alarm Listener Service")
    }

    SectionTitle("Configuration")
    FaListItem(
      modifier = Modifier.clickable {
        dispatch(v(navigate, flashPattern.patternsRoute))
      },
      secondaryText = { Text(stringResource(R.string.flash_pattern_desc)) }
    ) {
      FaListItemTitle(text = "Flashlight Pattern")
    }
    FaDivider()
    FaListItem(
      modifier = Modifier.clickable {
        dispatch(v(isAboutDialogVisible, true))
      }
    ) {
      FaListItemTitle(text = stringResource(id = R.string.about_label))
    }
  }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
  initAppDb()
  regBaseSubs()
  regHomeSubs()

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

@Preview(showBackground = true)
@Composable
fun HomePreview() {
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
