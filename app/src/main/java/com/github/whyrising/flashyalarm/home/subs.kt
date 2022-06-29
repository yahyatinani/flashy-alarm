package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.recompose.regSub

fun regHomeSubs() {
  regSub<AppDb, Boolean>(
    queryId = Ids.isDisableServiceDialogVisible,
  ) { db, _ ->
    db.isDisableServiceDialogVisible
  }
}
