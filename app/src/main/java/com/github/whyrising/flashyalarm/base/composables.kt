package com.github.whyrising.flashyalarm.base

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.whyrising.flashyalarm.base.Ids.formatScreenTitle
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v

@Composable
fun HostScreen(content: @Composable (padding: PaddingValues) -> Unit = {}) {
    FlashyAlarmTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(subscribe<String>(v(formatScreenTitle)).w())
                    },
                    elevation = 1.dp
                )
            },
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

// -- Previews -----------------------------------------------------------------

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    init()
    FlashyAlarmTheme {
        HostScreen()
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ScreenDarkPreview() {
    init()
    FlashyAlarmTheme {
        HostScreen()
    }
}
