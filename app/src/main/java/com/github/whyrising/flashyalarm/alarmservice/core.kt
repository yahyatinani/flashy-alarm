package com.github.whyrising.flashyalarm.alarmservice

import android.content.Context

/**
 * Initialize [AlarmListener] module.
 *
 * @param context passed by a lifecycle aware component.
 */
fun init(context: Context) {
  regCofx(context = context)
  regEvents()
  regSubs()
}
