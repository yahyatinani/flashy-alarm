package com.github.whyrising.flashyalarm.alarmlistener

import com.github.whyrising.flashyalarm.alarmlistener.Ids.fxEnableNotifAccess
import com.github.whyrising.flashyalarm.alarmlistener.Ids.isNotifAccessEnabled
import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema.db
import com.github.whyrising.y.collections.core.get
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

fun regEvents() {
    regEventFx(
        id = Ids.checkDeviceFlashlight,
        interceptors = v(injectCofx(Ids.isFlashAvailable)),
    ) { cofx, _ ->
        val appDb = cofx[db] as AppDb
        m(
            db to appDb.copy(
                alarmListenerDb = appDb.alarmListenerDb.copy(
                    isFlashSupported = cofx[Ids.isFlashAvailable] as Boolean
                )
            )
        )
    }

    regEventFx(
        id = isNotifAccessEnabled,
        interceptors = v(injectCofx(isNotifAccessEnabled))
    ) { cofx, _ ->
        val appDb = cofx[db] as AppDb
        val newDb = appDb.copy(
            alarmListenerDb = appDb.alarmListenerDb.copy(
                isNotifAccessEnabled = cofx[isNotifAccessEnabled] as Boolean
            )
        )
        m(db to newDb)
    }

    regEventFx(id = Ids.enableNotificationAccess) { _, _ ->
        m(fxEnableNotifAccess to true)
    }
}
