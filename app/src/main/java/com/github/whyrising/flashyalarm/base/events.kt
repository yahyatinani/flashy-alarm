package com.github.whyrising.flashyalarm.base

import com.github.whyrising.flashyalarm.Ids.exit_app
import com.github.whyrising.flashyalarm.Ids.navigate
import com.github.whyrising.flashyalarm.Ids.navigateFx
import com.github.whyrising.flashyalarm.Ids.update_screen_title
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.collections.core.m

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
}
