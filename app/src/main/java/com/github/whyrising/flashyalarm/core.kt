package com.github.whyrising.flashyalarm

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.github.whyrising.flashyalarm.alarmservice.AlarmService
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.checkDeviceFlashlight
import com.github.whyrising.flashyalarm.alarmservice.AlarmService.isFlashServiceRunning
import com.github.whyrising.flashyalarm.alarmservice.registerFlashlightFxs
import com.github.whyrising.flashyalarm.panel.common.HostScreen
import com.github.whyrising.flashyalarm.panel.common.appDb
import com.github.whyrising.flashyalarm.panel.common.common
import com.github.whyrising.flashyalarm.panel.common.common.exitApp
import com.github.whyrising.flashyalarm.panel.common.common.initAppDb
import com.github.whyrising.flashyalarm.panel.common.common.navigateFx
import com.github.whyrising.flashyalarm.panel.flashpattern.flashPatterns
import com.github.whyrising.flashyalarm.panel.flashpattern.initFlashPatternsModule
import com.github.whyrising.flashyalarm.panel.home.home
import com.github.whyrising.recompose.dispatch
import com.github.whyrising.recompose.dispatchSync
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regFx
import com.github.whyrising.recompose.watch
import com.github.whyrising.y.core.v
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import com.github.whyrising.flashyalarm.alarmservice.init as initAlarmListener
import com.github.whyrising.flashyalarm.panel.common.init as initBase
import com.github.whyrising.flashyalarm.panel.home.init as initHome

fun initAppDb() {
  regEventDb<Any>(initAppDb) { _, _ -> appDb }
  dispatchSync(v(initAppDb))
}

// -- Application Implementation -----------------------------------------------

class MyApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    initAppDb()
  }
}

// -- Entry Point --------------------------------------------------------------

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
  @Composable
  fun PhoneWithoutTorchAlertDialog() {
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
          }
        ) {
          Text(text = stringResource(R.string.alert_btn_exit))
        }
      }
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    initAlarmListener(context = this)
    dispatch(v(checkDeviceFlashlight))
    initBase(this)
    registerFlashlightFxs(this)
    initHome(this)
    initFlashPatternsModule(this)

    if (Build.VERSION.SDK_INT >= 33) {
      if (ContextCompat.checkSelfPermission(
          this,
          POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        requestPermissions(arrayOf(POST_NOTIFICATIONS), 101)
      }
    }
    setContent {
      HostScreen {
        if (watch(v(AlarmService.isFlashHardwareAvailable))) {
          val systemUiController = rememberSystemUiController()
          val colors = MaterialTheme.colorScheme
          SideEffect {
            systemUiController.setSystemBarsColor(
              color = colors.primary,
              darkIcons = true
            )
          }
          val navCtrl = rememberAnimatedNavController().apply {
            addOnDestinationChangedListener { controller, navDestination, _ ->
              val flag = controller.previousBackStackEntry != null
              dispatch(v(common.setBackstackStatus, flag))
              val route = navDestination.route
              if (route != null) {
                dispatch(v(common.updateScreenTitle, route))
              }
            }
          }
          LaunchedEffect(key1 = navCtrl) {
            regFx(navigateFx) { route ->
              when (val r = "$route") {
                common.goBack.name -> navCtrl.popBackStack()
                else -> runBlocking(Dispatchers.Main.immediate) {
                  navCtrl.navigate(r)
                }
              }
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
        } else {
          PhoneWithoutTorchAlertDialog()
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()

    dispatch(v(isFlashServiceRunning))
  }
}
