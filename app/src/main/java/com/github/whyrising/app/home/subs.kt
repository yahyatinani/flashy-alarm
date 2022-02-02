package com.github.whyrising.app.home

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.github.whyrising.app.Keys.android_greeting
import com.github.whyrising.app.Keys.counter
import com.github.whyrising.app.Keys.is_about_btn_enabled
import com.github.whyrising.app.Keys.sdk_version
import com.github.whyrising.app.global.DbSchema
import com.github.whyrising.recompose.regSub
import com.github.whyrising.recompose.subscribe
import com.github.whyrising.y.collections.core.v

fun regHomeSubs() {
    regSub<DbSchema, Boolean>(
        queryId = is_about_btn_enabled,
    ) { db, _ ->
        db.home.isAboutBtnEnabled
    }

    regSub<DbSchema, Int>(
        queryId = sdk_version,
    ) { db, _ ->
        db.home.sdkVersion
    }

    regSub(
        queryId = android_greeting,
        signalsFn = { subscribe(v(sdk_version)) },
    ) { sdkVersion: Int, (_, color) ->
        buildAnnotatedString {
            val style = SpanStyle(fontSize = 32.sp)
            val space = " "

            withStyle(style) {
                append("Hello")
            }

            append(space)

            withStyle(
                style.copy(
                    color = color as Color,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Android $sdkVersion")
            }

            append(space)

            withStyle(style) {
                append("\uD83D\uDE00")
            }
        }
    }

    regSub<DbSchema, String>(
        queryId = counter,
    ) { db, _ ->
        "${db.home.count}"
    }
}
