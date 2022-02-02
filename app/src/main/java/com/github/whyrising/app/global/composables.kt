package com.github.whyrising.app.global

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.whyrising.app.Keys.flashLight
import com.github.whyrising.app.Keys.format_screen_title
import com.github.whyrising.app.Keys.toggle_theme
import com.github.whyrising.app.initAppDb
import com.github.whyrising.app.ui.theme.JetpackComposeTemplateTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v

@Composable
fun HostScreen(content: @Composable (padding: PaddingValues) -> Unit = {}) {
    JetpackComposeTemplateTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(subscribe<String>(v(format_screen_title)).w())
                    },
                    elevation = 1.dp,
                    actions = {
                        IconButton(
                            onClick = { dispatch(v(toggle_theme)) },
                        ) {
                            val ic = subscribe<ImageVector>(v(flashLight)).w()
                            Icon(
                                imageVector = ic,
                                contentDescription = "Theme button"
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}

// -- Previews -----------------------------------------------------------------

private fun init() {
    initAppDb()
    regGlobalSubs()
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    init()
    JetpackComposeTemplateTheme {
        HostScreen()
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ScreenDarkPreview() {
    init()
    JetpackComposeTemplateTheme {
        HostScreen()
    }
}
