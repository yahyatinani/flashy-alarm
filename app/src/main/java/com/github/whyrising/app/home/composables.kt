package com.github.whyrising.app.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.whyrising.app.Keys.android_greeting
import com.github.whyrising.app.Keys.counter
import com.github.whyrising.app.Keys.enable_about_btn
import com.github.whyrising.app.Keys.inc_counter
import com.github.whyrising.app.Keys.is_about_btn_enabled
import com.github.whyrising.app.Keys.navigate_about
import com.github.whyrising.app.Keys.set_android_version
import com.github.whyrising.app.Keys.update_screen_title
import com.github.whyrising.app.R
import com.github.whyrising.app.initAppDb
import com.github.whyrising.app.ui.theme.JetpackComposeTemplateTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v

var start: Long = System.currentTimeMillis()

@Composable
fun HomeScreen() {
    regHomeCofx()
    regHomeEvents()
    regHomeSubs()

    val title = stringResource(R.string.home_screen_title)
    SideEffect {
        dispatch(v(update_screen_title, title))
        dispatch(v(set_android_version))
        dispatch(v(enable_about_btn))
    }

    val primaryColor = MaterialTheme.colors.primary
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = subscribe<AnnotatedString>(
                    qvec = v(android_greeting, primaryColor)
                ).w(),
                modifier = Modifier.padding(24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = subscribe<String>(v(counter)).w(),
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { dispatch(v(inc_counter)) }) {
                Text(text = "Count")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    start = System.currentTimeMillis()
                    dispatch(v(navigate_about))
                },
                enabled = subscribe<Boolean>(v(is_about_btn_enabled)).w()
            ) {
                Text(text = "About")
            }
        }
    }
}

// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    initAppDb()
    JetpackComposeTemplateTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ScreenDarkPreview() {
    initAppDb()
    JetpackComposeTemplateTheme {
        HomeScreen()
    }
}
