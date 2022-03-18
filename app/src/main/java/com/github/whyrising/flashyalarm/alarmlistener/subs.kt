package com.github.whyrising.flashyalarm.alarmlistener

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.regSub

fun regSubs() {
    regSub<AppDb, Boolean>(
        queryId = Ids.isNotifAccessEnabled,
    ) { db, _ ->
        db.alarmListenerDb.isNotifAccessEnabled
    }

    regSub<AppDb, Boolean>(
        queryId = Ids.isFlashAvailable,
    ) { db, _ ->
        db.alarmListenerDb.isFlashSupported
    }
}
