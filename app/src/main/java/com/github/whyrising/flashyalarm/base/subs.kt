package com.github.whyrising.flashyalarm.base

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.whyrising.flashyalarm.Ids.flashlight
import com.github.whyrising.flashyalarm.Ids.format_screen_title
import com.github.whyrising.flashyalarm.Ids.isDark
import com.github.whyrising.flashyalarm.Ids.screen_title
import com.github.whyrising.recompose.regSub
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.y.collections.core.v

fun regBaseSubs() {
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
        queryId = flashlight,
        signalsFn = { subscribe(v(isDark)) },
    ) { isDarkMode, _ ->
        when {
            isDarkMode -> Icons.Default.FlashlightOff
            else -> Icons.Default.FlashlightOn
        }
    }
}
