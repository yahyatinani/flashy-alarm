package com.github.whyrising.flashyalarm.about

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.whyrising.flashyalarm.Keys.sdk_version
import com.github.whyrising.flashyalarm.Keys.update_screen_title
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v

@Composable
fun AboutScreen() {
    val title = stringResource(R.string.about_screen_title)
    SideEffect {
        dispatch(v(update_screen_title, title))
    }
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = "This app is a template for building new Compose apps.",
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Android API: ${subscribe<Int>(v(sdk_version)).w()}",
                color = MaterialTheme.colors.primary,
                fontSize = 20.sp
            )
        }
    }
}

// -- Previews -----------------------------------------------------------------

private fun init() {
    initAppDb()
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    init()
    FlashyAlarmTheme {
        AboutScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ScreenDarkPreview() {
    init()
    FlashyAlarmTheme {
        AboutScreen()
    }
}
