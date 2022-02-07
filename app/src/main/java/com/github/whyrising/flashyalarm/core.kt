package com.github.whyrising.flashyalarm

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.flashyalarm.Ids.exit_app
import com.github.whyrising.flashyalarm.Ids.fx_enable_notif_access
import com.github.whyrising.flashyalarm.Ids.is_notif_access_enabled
import com.github.whyrising.flashyalarm.Ids.navigateFx
import com.github.whyrising.flashyalarm.base.HostScreen
import com.github.whyrising.flashyalarm.base.defaultDb
import com.github.whyrising.flashyalarm.base.regBaseEvents
import com.github.whyrising.flashyalarm.base.regBaseSubs
import com.github.whyrising.flashyalarm.home.HomeScreen
import com.github.whyrising.flashyalarm.notificationdialog.regNotifDialogCofx
import com.github.whyrising.flashyalarm.notificationdialog.regNotifDialogEvents
import com.github.whyrising.flashyalarm.notificationdialog.regNotifDialogSubs
import com.github.whyrising.flashyalarm.ui.animation.nav.enterAnimation
import com.github.whyrising.flashyalarm.ui.animation.nav.exitAnimation
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.dispatchSync
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.collections.core.v
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
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

    // Subs
    screen_title,
    format_screen_title,
    is_notif_access_enabled,

    // Fx
    navigateFx,
    fx_enable_notif_access,
    flash_on,
}

// -- Routing ------------------------------------------------------------------
@ExperimentalAnimationApi
fun NavGraphBuilder.home(animOffSetX: Int) {
    composable(
        route = home_route,
        exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
        popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
    ) {
        HomeScreen()
    }
}

@ExperimentalAnimationApi
@Composable
fun Navigation(padding: PaddingValues) {
    val navController = rememberAnimatedNavController()
    LaunchedEffect(navController) {
        regFx(id = navigateFx) { route ->
            if (route == null) return@regFx
            navController.navigate("$route")
        }
    }

    AnimatedNavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = home_route
    ) {
        home(animOffSetX = 300)
    }
}

// -- Entry Point --------------------------------------------------------------

fun initAppDb() {
    regEventDb<Any>(":init-db") { _, _ -> defaultDb }
    dispatchSync(v(":init-db"))
}

class MainActivity : ComponentActivity() {
    private val userAllowsAccess = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {}

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        initAppDb()
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

        setContent {
            val systemUiController = rememberSystemUiController()

            HostScreen {
                val colors = MaterialTheme.colors
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = colors.primary,
                        darkIcons = true
                    )
                }
                Navigation(padding = it)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        dispatch(v(is_notif_access_enabled))
    }
}
