package com.github.whyrising.flashyalarm.base

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.whyrising.flashyalarm.base.base.formatScreenTitle
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.theme.BackArrow
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.core.v

@Composable
fun HostScreen(content: @Composable (padding: PaddingValues) -> Unit = {}) {
  FlashyAlarmTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Text(subscribe<String>(v(formatScreenTitle)).w())
          },
          elevation = 1.dp,
          navigationIcon = when {
            subscribe<Boolean>(v(base.isBackstackAvailable)).w() -> {
              { BackArrow() }
            }
            else -> null
          }
        )
      },
    ) { innerPadding ->
      content(innerPadding)
    }
  }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
  initAppDb()
  regBaseSubs()
  FlashyAlarmTheme {
    HostScreen()
  }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ScreenDarkPreview() {
  FlashyAlarmTheme {
    HostScreen()
  }
}
