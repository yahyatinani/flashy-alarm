package com.github.whyrising.flashyalarm

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.github.whyrising.flashyalarm.alarmservice.AlarmService
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.checkDeviceFlashlight
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.isFlashServiceRunning
import com.github.whyrising.flashyalarm.alarmservice.FlashyAlarmService
import com.github.whyrising.flashyalarm.alarmservice.registerFlashlightFxs
import com.github.whyrising.flashyalarm.base.HostScreen
import com.github.whyrising.flashyalarm.base.appDb
import com.github.whyrising.flashyalarm.base.base
import com.github.whyrising.flashyalarm.base.base.exitApp
import com.github.whyrising.flashyalarm.base.base.initAppDb
import com.github.whyrising.flashyalarm.base.base.navigateFx
import com.github.whyrising.flashyalarm.flashpattern.flashPatterns
import com.github.whyrising.flashyalarm.flashpattern.initFlashPatternsModule
import com.github.whyrising.flashyalarm.home.home
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.dispatchSync
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.recompose.w
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.github.whyrising.flashyalarm.alarmservice.init as initAlarmListener
import com.github.whyrising.flashyalarm.base.init as initBase
import com.github.whyrising.flashyalarm.home.init as initHome

// -- Entry Point --------------------------------------------------------------

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
          dispatch(v(exitApp))
        },
      ) {
        Text(text = stringResource(R.string.alert_btn_exit))
      }
    }
  )
}

fun initAppDb() {
  regEventDb<Any>(initAppDb) { _, _ -> appDb }
  dispatchSync(v(initAppDb))
}

class MyApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    initAppDb()
  }
}

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    installSplashScreen()
    super.onCreate(savedInstanceState)

    Log.i("FlashyAlarmService", "${FlashyAlarmService.isServiceRunning}")
    initAlarmListener(context = this)
    dispatch(v(checkDeviceFlashlight))
    initBase(this)
    registerFlashlightFxs(this)
    initHome(this)
    initFlashPatternsModule(this)

    setContent {
      HostScreen {
        if (subscribe<Boolean>(v(AlarmService.isFlashHardwareAvailable)).w()) {
          val systemUiController = rememberSystemUiController()
          val colors = MaterialTheme.colors
          SideEffect {
            systemUiController.setSystemBarsColor(
              color = colors.primary,
              darkIcons = true
            )
          }
          val navCtrl = rememberAnimatedNavController().apply {
            addOnDestinationChangedListener { controller, navDestination, _ ->
              val flag = controller.previousBackStackEntry != null
              dispatch(v(base.setBackstackStatus, flag))
              val route = navDestination.route
              if (route != null)
                dispatch(v(base.updateScreenTitle, route))
            }
          }
          regFx(navigateFx) { route ->
            when (val r = "$route") {
              base.goBack.name -> navCtrl.popBackStack()
              else -> navCtrl.navigate(r)
            }
          }
          AnimatedNavHost(
            modifier = Modifier.padding(it),
            navController = navCtrl,
            startDestination = home.homeRoute.name
          ) {
            home(animOffSetX = 300)
            flashPatterns(animOffSetX = 300)
          }
        } else NoFlashAlertDialog()
      }
    }
  }

  override fun onResume() {
    super.onResume()

    dispatch(v(isFlashServiceRunning))
  }
}
