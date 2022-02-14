package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.Ids.check_device_flashlight
import com.github.whyrising.flashyalarm.Ids.exit_app
import com.github.whyrising.flashyalarm.Ids.is_flash_available
import com.github.whyrising.flashyalarm.Ids.navigate
import com.github.whyrising.flashyalarm.Ids.navigateFx
import com.github.whyrising.flashyalarm.Ids.update_screen_title
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema.db
import com.github.whyrising.y.collections.core.get
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

fun regBaseEvents() {
    regEventDb<DbSchema>(id = update_screen_title) { db, (_, title) ->
        db.copy(screenTitle = title as String)
    }

    regEventFx(id = navigate) { _, (_, route) ->
        m(navigateFx to route)
    }

    regEventFx(id = exit_app) { _, _ ->
        m(exit_app to -1)
    }

    regEventFx(
        id = check_device_flashlight,
        interceptors = v(injectCofx(is_flash_available)),
    ) { cofx, _ ->
        val appDb = cofx[db] as DbSchema
        m(
            db to appDb.copy(
                isFlashSupported = cofx[is_flash_available] as Boolean
            )
        )
    }
}
