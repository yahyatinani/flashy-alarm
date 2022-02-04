package com.github.whyrising.flashyalarm.notificationdialog

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.whyrising.flashyalarm.Ids
import com.github.whyrising.flashyalarm.Ids.enable_notification_access
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.y.collections.core.v

@Composable
fun EnableNotificationAlertDialog() {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = { Text(text = "Important") },
        text = {
            Text(
                text = stringResource(R.string.notif_alert_msg)
                    .format(stringResource(R.string.app_name))
            )
        },
        confirmButton = {
            Button(onClick = { dispatch(v(enable_notification_access)) }) {
                Text(text = "Enable")
            }
        },
        dismissButton = {
            Button(onClick = { dispatch(v(Ids.exit_app)) }) {
                Text(text = "Exit App")
            }
        }
    )
}
