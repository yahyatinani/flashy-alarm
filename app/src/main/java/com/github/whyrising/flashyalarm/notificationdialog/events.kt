package com.github.whyrising.flashyalarm.notificationdialog

import com.github.whyrising.flashyalarm.Ids.enable_notification_access
import com.github.whyrising.flashyalarm.Ids.fx_enable_notif_access
import com.github.whyrising.flashyalarm.Ids.is_notif_access_enabled
import com.github.whyrising.flashyalarm.global.DbSchema
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema
import com.github.whyrising.y.collections.core.get
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

fun regNotifDialogEvents() {
    regEventFx(
        id = is_notif_access_enabled,
        interceptors = v(injectCofx(is_notif_access_enabled))
    ) { cofx, _ ->
        val appDb = cofx[Schema.db] as DbSchema
        val newDb = appDb.copy(
            isNotifAccessEnabled = cofx[is_notif_access_enabled] as Boolean
        )
        m(Schema.db to newDb)
    }

    regEventFx(id = enable_notification_access) { _, _ ->
        m(fx_enable_notif_access to true)
    }
}
