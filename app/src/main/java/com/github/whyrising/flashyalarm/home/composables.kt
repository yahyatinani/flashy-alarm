package com.github.whyrising.flashyalarm.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.github.whyrising.flashyalarm.Ids.android_greeting
import com.github.whyrising.flashyalarm.Ids.counter
import com.github.whyrising.flashyalarm.Ids.inc_counter
import com.github.whyrising.flashyalarm.Ids.is_notif_access_enabled
import com.github.whyrising.flashyalarm.Ids.set_android_version
import com.github.whyrising.flashyalarm.Ids.update_screen_title
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.global.regGlobalSubs
import com.github.whyrising.flashyalarm.initAppDb
import com.github.whyrising.flashyalarm.notificationdialog.EnableNotificationAlertDialog
import com.github.whyrising.flashyalarm.ui.theme.FlashyAlarmTheme
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v

const val route = "/home"

@Composable
fun HomeScreen(context: Context) {
    regHomeCofx()
    regHomeEvents()
    regHomeSubs()

    if (!subscribe<Boolean>(v(is_notif_access_enabled)).w()) {
        EnableNotificationAlertDialog()
    }

    val title = stringResource(R.string.home_screen_title)
    SideEffect {
        dispatch(v(update_screen_title, title))
        dispatch(v(set_android_version))
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
                    sendNotification(context)
                }
            ) {
                Text(text = "About")
            }
        }
    }
}

fun sendNotification(
    context: Context,
    title: String = "Sup!",
    body: String = "Body"
) {
    val manager = NotificationManagerCompat.from(context)
    val channelId = "notification_channel_firing_alarm_and_timer"
    val CHANNEL_NAME = "whychan"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        manager.createNotificationChannel(
            NotificationChannel(
                channelId, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }

    val notificationBuilder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(title)
        .setContentText(body)
        .setCategory("alarm")

    manager.notify(578454, notificationBuilder.build())
}
// -- Previews -----------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    initAppDb()
    regGlobalSubs()
    FlashyAlarmTheme {
//        HomeScreen()
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ScreenDarkPreview() {
    initAppDb()
    regGlobalSubs()
    FlashyAlarmTheme {
//        HomeScreen()
    }
}
