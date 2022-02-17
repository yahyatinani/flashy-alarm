package com.github.whyrising.flashyalarm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.whyrising.flashyalarm.Ids.check_device_flashlight
import com.github.whyrising.flashyalarm.Ids.exit_app
import com.github.whyrising.flashyalarm.Ids.fx_enable_notif_access
import com.github.whyrising.flashyalarm.Ids.is_notif_access_enabled
import com.github.whyrising.flashyalarm.Ids.navigateFx
import com.github.whyrising.flashyalarm.base.HostScreen
import com.github.whyrising.flashyalarm.base.defaultDb
import com.github.whyrising.flashyalarm.base.regBaseCofx
import com.github.whyrising.flashyalarm.base.regBaseEvents
import com.github.whyrising.flashyalarm.base.regBaseSubs
import com.github.whyrising.flashyalarm.home.home
import com.github.whyrising.flashyalarm.home.regHomeEvents
import com.github.whyrising.flashyalarm.home.regHomeSubs
import com.github.whyrising.flashyalarm.notificationdialog.regNotifDialogCofx
import com.github.whyrising.flashyalarm.notificationdialog.regNotifDialogEvents
import com.github.whyrising.flashyalarm.notificationdialog.regNotifDialogSubs
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.dispatchSync
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.collections.core.v
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.system.exitProcess
import com.github.whyrising.flashyalarm.home.route as home_route

@Suppress("EnumEntryName")
enum class Ids {
    // Events
    update_screen_title,
    navigate,
    enable_notification_access,
    exit_app,
    stop_alarm_listener,
    check_device_flashlight,

    // Subs
    screen_title,
    format_screen_title,
    is_notif_access_enabled,
    show_dialog,
    is_flash_available,

    // Fx
    navigateFx,
    fx_enable_notif_access,
    flash_on,
}

// -- Entry Point --------------------------------------------------------------

fun initAppDb() {
    regEventDb<Any>(":init-db") { _, _ -> defaultDb }
    dispatchSync(v(":init-db"))
}

@Composable
fun NoFlashAlertDialog() {
    AlertDialog(
        onDismissRequest = { /*TODO*/ },
        title = {
            Text(text = stringResource(R.string.alert_title_important))
        },
        text = {
            Text(
                text = stringResource(R.string.alert_msg_no_flashlight)
            )
        },
        confirmButton = {},
        dismissButton = {
            Button(
                onClick = {
                    dispatch(v(exit_app, true))
                },
            ) {
                Text(text = stringResource(R.string.alert_btn_exit))
            }
        }
    )
}

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    private val userAllowsAccess = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        initAppDb()
        regBaseCofx(this)
        regBaseEvents()
        regBaseSubs()
        regNotifDialogEvents()
        regNotifDialogCofx(context = this)
        regNotifDialogSubs()
        regFx(id = exit_app) { value ->
            exitProcess(value as Int)
        }
        regFx(id = fx_enable_notif_access) {
            val intent = Intent(
                "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
            )
            userAllowsAccess.launch(intent)
        }
        regHomeEvents()
        regHomeSubs()
        dispatch(v(check_device_flashlight))

        setContent {
            HostScreen {
                if (!subscribe<Boolean>(v(Ids.is_flash_available)).w())
                    NoFlashAlertDialog()
                else {
                    val systemUiController = rememberSystemUiController()
                    val colors = MaterialTheme.colors
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = colors.primary,
                            darkIcons = true
                        )
                    }
                    val navCtrl = rememberAnimatedNavController()
                    LaunchedEffect(navCtrl) {
                        regFx(id = navigateFx) { route ->
                            if (route == null) return@regFx
                            navCtrl.navigate("$route")
                        }
                    }

                    AnimatedNavHost(
                        modifier = Modifier.padding(it),
                        navController = navCtrl,
                        startDestination = home_route
                    ) {
                        home(animOffSetX = 300)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        dispatch(v(is_notif_access_enabled))
    }
}
