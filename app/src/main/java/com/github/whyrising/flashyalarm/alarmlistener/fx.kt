package com.github.whyrising.flashyalarm.alarmlistener

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.animation.ExperimentalAnimationApi
import com.github.whyrising.recompose.regFx

const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"
const val EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key"
const val EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args"

/**
 * Call this function at the start of your app to initialize the necessary
 * [AlarmListener] service effects.
 * @param context passed by an activity or application.
 */
@ExperimentalAnimationApi
fun regFx(context: Context) {
    regFx(id = Ids.fxEnableNotifAccess) {
        val intent = Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS)
        val packageName = context.packageName
        val toHighlight = "$packageName/${AlarmListener::class.java.name}"
        val bundle = Bundle()
        bundle.putString(EXTRA_FRAGMENT_ARG_KEY, toHighlight)
        intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, toHighlight)
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle)
        context.startActivity(intent)
    }
}
