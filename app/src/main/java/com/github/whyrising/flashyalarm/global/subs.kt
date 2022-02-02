package com.github.whyrising.flashyalarm.global

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.whyrising.flashyalarm.Keys.flashLight
import com.github.whyrising.flashyalarm.Keys.format_screen_title
import com.github.whyrising.flashyalarm.Keys.isDark
import com.github.whyrising.flashyalarm.Keys.screen_title
import com.github.whyrising.recompose.regSub
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.y.collections.core.v

fun regGlobalSubs() {
    regSub<DbSchema, String>(
        queryId = screen_title,
    ) { db, _ ->
        db.screenTitle
    }

    regSub<String, String>(
        queryId = format_screen_title,
        signalsFn = { subscribe(v(screen_title)) }
    ) { title, _ ->
        title.replaceFirstChar { it.uppercase() }
    }

    regSub<DbSchema, Boolean>(
        queryId = isDark,
    ) { db, _ ->
        db.isDark
    }

    regSub<Boolean, ImageVector>(
        queryId = flashLight,
        signalsFn = { subscribe(v(isDark)) },
    ) { isDarkMode, _ ->
        when {
            isDarkMode -> Icons.Default.FlashlightOff
            else -> Icons.Default.FlashlightOn
        }
    }
}
