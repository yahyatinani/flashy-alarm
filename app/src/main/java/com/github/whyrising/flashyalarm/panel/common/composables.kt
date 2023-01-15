package com.github.whyrising.flashyalarm.panel.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.whyrising.flashyalarm.designsystem.theme.FlashyAlarmTheme
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.panel.common.common.formatScreenTitle
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.watch
import com.github.whyrising.y.core.v

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostScreen(content: @Composable (padding: PaddingValues) -> Unit = {}) {
  FlashyAlarmTheme {
    Scaffold(
      topBar = {
        TopAppBar(
          title = {
            Text(text = watch<String>(query = v(formatScreenTitle)))
          },
          navigationIcon = {
            if (watch(v(common.isBackstackAvailable))) {
              IconButton(
                onClick = {
                  dispatch(v(common.navigate, common.goBack))
                }
              ) {
                Icon(
                  imageVector = Icons.Filled.ArrowBack,
                  contentDescription = "Back"
                )
              }
            }
          }
        )
      }
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
