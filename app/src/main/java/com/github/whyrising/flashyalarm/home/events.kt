package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.Keys
import com.github.whyrising.flashyalarm.Keys.disable_about_btn
import com.github.whyrising.flashyalarm.Keys.enable_about_btn
import com.github.whyrising.flashyalarm.Keys.inc_counter
import com.github.whyrising.flashyalarm.Keys.navigateFx
import com.github.whyrising.flashyalarm.Keys.navigate_about
import com.github.whyrising.flashyalarm.Keys.sdk_version
import com.github.whyrising.flashyalarm.Routes
import com.github.whyrising.flashyalarm.global.DbSchema
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.fx.FxIds.fx
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema.db
import com.github.whyrising.y.collections.core.get
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

fun disableAboutBtn(appDb: DbSchema) = appDb.copy(
    home = appDb.home.copy(isAboutBtnEnabled = false)
)

fun regHomeEvents() {
    regEventDb<DbSchema>(id = enable_about_btn) { db, _ ->
        db.copy(home = db.home.copy(isAboutBtnEnabled = true))
    }

    regEventDb<DbSchema>(id = disable_about_btn) { db, _ ->
        disableAboutBtn(db)
    }

    regEventFx(
        id = Keys.set_android_version,
        interceptors = v(injectCofx(sdk_version)),
    ) { cofx, _ ->
        val appDb = cofx[db] as DbSchema
        m(
            db to appDb.copy(
                home = appDb.home.copy(sdkVersion = cofx[sdk_version] as Int)
            )
        )
    }

    regEventFx(id = navigate_about) { cofx, _ ->
        m(
            db to disableAboutBtn(cofx[db] as DbSchema),
            fx to v(v(navigateFx, Routes.about))
        )
    }

    regEventDb<DbSchema>(id = inc_counter) { db, _ ->
        val home = db.home
        db.copy(home = home.copy(count = home.count.inc()))
    }
}
