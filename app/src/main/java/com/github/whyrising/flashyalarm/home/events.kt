package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.Ids.show_dialog
import com.github.whyrising.flashyalarm.base.DbSchema
import com.github.whyrising.recompose.regEventDb

fun regHomeEvents() {
    regEventDb<DbSchema>(id = show_dialog) { db, (_, flag) ->
        db.copy(showAlertDialog = flag as Boolean)
    }
}
