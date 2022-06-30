package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.regEventDb

fun regHomeEvents() {
  regEventDb<AppDb>(id = Ids.showDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = true)
  }
  regEventDb<AppDb>(id = Ids.hideDisableServiceDialog) { db, _ ->
    db.copy(isDisableServiceDialogVisible = false)
  }
}
