package com.github.whyrising.flashyalarm.global

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import com.github.whyrising.flashyalarm.Ids.exit_app
import com.github.whyrising.flashyalarm.Ids.navigate
import com.github.whyrising.flashyalarm.Ids.navigateFx
import com.github.whyrising.flashyalarm.Ids.setDarkMode
import com.github.whyrising.flashyalarm.Ids.toggle_theme
import com.github.whyrising.flashyalarm.Ids.update_screen_title
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.y.collections.core.m

fun isSystemDarkMode(uiMode: Int) =
    uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES

fun regGlobalEvents() {
    regEventDb<DbSchema>(id = update_screen_title) { db, (_, title) ->
        db.copy(screenTitle = title as String)
    }

    regEventDb<DbSchema>(id = toggle_theme) { db, _ ->
        db.copy(isDark = !db.isDark)
    }

    regEventDb<DbSchema>(id = setDarkMode) { db, (_, uiMode) ->
        db.copy(isDark = isSystemDarkMode(uiMode as Int))
    }

    regEventFx(id = navigate) { _, (_, route) ->
        m(navigateFx to route)
    }

    regEventFx(id = exit_app) { _, _ ->
        m(exit_app to -1)
    }
}
