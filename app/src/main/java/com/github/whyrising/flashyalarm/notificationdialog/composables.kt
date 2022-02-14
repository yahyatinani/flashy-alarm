package com.github.whyrising.flashyalarm.notificationdialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.whyrising.flashyalarm.Ids.show_dialog
import com.github.whyrising.flashyalarm.Ids.stop_alarm_listener
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.y.collections.core.v

@Composable
fun DisableServiceAlertDialog() {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(text = stringResource(R.string.alert_title_important))
        },
        text = {
            Text(
                text = stringResource(R.string.notif_alert_msg)
                    .format(stringResource(R.string.app_name))
            )
        },
        confirmButton = {
            Button(onClick = {
                dispatch(v(stop_alarm_listener))
            }) {
                Text(text = stringResource(R.string.alert_btn_yes))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    dispatch(v(show_dialog, false))
                },
            ) {
                Text(text = stringResource(R.string.alert_btn_exit))
            }
        }
    )
}
