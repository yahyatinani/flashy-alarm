package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.Ids
import com.github.whyrising.flashyalarm.Ids.inc_counter
import com.github.whyrising.flashyalarm.Ids.sdk_version
import com.github.whyrising.flashyalarm.global.DbSchema
import com.github.whyrising.recompose.cofx.injectCofx
import com.github.whyrising.recompose.regEventDb
import com.github.whyrising.recompose.regEventFx
import com.github.whyrising.recompose.schemas.Schema.db
import com.github.whyrising.y.collections.core.get
import com.github.whyrising.y.collections.core.m
import com.github.whyrising.y.collections.core.v

fun regHomeEvents() {
    regEventFx(
        id = Ids.set_android_version,
        interceptors = v(injectCofx(sdk_version)),
    ) { cofx, _ ->
        val appDb = cofx[db] as DbSchema
        m(
            db to appDb.copy(
                home = appDb.home.copy(sdkVersion = cofx[sdk_version] as Int)
            )
        )
    }

    regEventDb<DbSchema>(id = inc_counter) { db, _ ->
        val home = db.home
        db.copy(home = home.copy(count = home.count.inc()))
    }
}
