package com.github.whyrising.flashyalarm.notificationdialog

import com.github.whyrising.flashyalarm.Ids.is_notif_access_enabled
import com.github.whyrising.flashyalarm.global.DbSchema
import com.github.whyrising.recompose.regSub

fun regNotifDialogSubs() {
    regSub<DbSchema, Boolean>(
        queryId = is_notif_access_enabled,
    ) { db, _ ->
        db.isNotifAccessEnabled
    }
}
