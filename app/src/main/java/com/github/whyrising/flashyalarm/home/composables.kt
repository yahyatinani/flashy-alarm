package com.github.whyrising.flashyalarm.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.whyrising.flashyalarm.Ids
import com.github.whyrising.flashyalarm.Ids.is_notif_access_enabled
import com.github.whyrising.flashyalarm.Ids.show_dialog
import com.github.whyrising.flashyalarm.Ids.update_screen_title
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.base.DbSchema
import com.github.whyrising.flashyalarm.base.regBaseSubs
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.notificationdialog.DisableServiceAlertDialog
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.regSub
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v

const val route = "/home"

@Composable
fun HomeScreen() {
    val colors = MaterialTheme.colors
    val title = stringResource(R.string.home_screen_title)
    dispatch(v(update_screen_title, title))
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SideEffect {
                dispatch(v(is_notif_access_enabled))
            }
            Text(
                stringResource(R.string.service_switch_label),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = subscribe<Boolean>(v(is_notif_access_enabled)).w(),
                onCheckedChange = {
                    when {
                        it -> dispatch(v(Ids.enable_notification_access))
                        else -> {
                            dispatch(v(show_dialog, true))
                        }
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colors.primary,
                    checkedTrackColor = colors.primary,
                )
            )

            if (subscribe<Boolean>(v(show_dialog)).w())
                DisableServiceAlertDialog()
        }
    }
}

// -- Previews -----------------------------------------------------------------

private fun initPreview() {
    initAppDb()
    regBaseSubs()
    regSub<DbSchema, Boolean>(
        queryId = is_notif_access_enabled,
    ) { _, _ ->
        true
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    initPreview()
    FlashyAlarmTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ScreenDarkPreview() {
    initPreview()
    FlashyAlarmTheme(darkTheme = true) {
        HomeScreen()
    }
}
