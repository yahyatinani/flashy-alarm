package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.Ids.show_dialog
import com.github.whyrising.flashyalarm.base.DbSchema
import com.github.whyrising.recompose.regSub

fun regHomeSubs() {
    regSub<DbSchema, Boolean>(
        queryId = show_dialog,
    ) { db, _ ->
        db.showAlertDialog
    }
}
