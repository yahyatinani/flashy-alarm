package com.github.whyrising.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraphBuilder
import com.github.whyrising.app.Keys.navigateFx
import com.github.whyrising.app.about.AboutScreen
import com.github.whyrising.app.global.HostScreen
import com.github.whyrising.app.global.defaultDb
import com.github.whyrising.app.global.regGlobalEvents
import com.github.whyrising.app.global.regGlobalSubs
import com.github.whyrising.app.home.HomeScreen
import com.github.whyrising.app.ui.animation.nav.enterAnimation
import com.github.whyrising.app.ui.animation.nav.exitAnimation
import com.github.whyrising.recompose.dispatchSync
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regFx
import com.github.whyrising.y.collections.core.v
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

// -- Routes & Navigation ------------------------------------------------------

object Routes {
    const val home = "/home"
    const val about = "/about"
}

@Suppress("EnumEntryName")
enum class Keys {
    // Events
    enable_about_btn,
    disable_about_btn,
    set_android_version,
    update_screen_title,
    navigate_about,
    navigate,
    inc_counter,
    toggle_theme,
    setDarkMode,
    isDark,

    // Subs
    sdk_version,
    screen_title,
    format_screen_title,
    is_about_btn_enabled,
    android_greeting,
    counter,
    flashLight,
    uiMode,

    // Fx
    navigateFx,
}

@ExperimentalAnimationApi
fun NavGraphBuilder.homeComposable(animOffSetX: Int) {
    composable(
        route = Routes.home,
        exitTransition = { exitAnimation(targetOffsetX = -animOffSetX) },
        popEnterTransition = { enterAnimation(initialOffsetX = -animOffSetX) }
    ) {
        HomeScreen()
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.aboutComposable(animOffSetX: Int) {
    composable(
        route = Routes.about,
        enterTransition = { enterAnimation(initialOffsetX = animOffSetX) },
        popExitTransition = { exitAnimation(targetOffsetX = animOffSetX) },
    ) {
        AboutScreen()
    }
}

@ExperimentalAnimationApi
@Composable
fun Navigation(padding: PaddingValues) {
    val navController = rememberAnimatedNavController()
    LaunchedEffect(navController) {
        regFx(id = navigateFx) { route ->
            if (route == null)
                return@regFx

            navController.navigate("$route")
        }
    }

    AnimatedNavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = Routes.home
    ) {
        homeComposable(animOffSetX = 300)
        aboutComposable(animOffSetX = 300)
    }
}

// -- Entry Point --------------------------------------------------------------

fun initAppDb() {
    regEventDb<Any>(":init-db") { _, _ -> defaultDb }
    dispatchSync(v(":init-db"))
}

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        initAppDb()
        regGlobalEvents()
        regGlobalSubs()

        setContent {
            HostScreen {
                Navigation(padding = it)
            }
        }
    }
}
