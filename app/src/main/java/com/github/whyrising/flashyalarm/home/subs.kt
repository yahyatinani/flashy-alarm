package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.base.Ids.isAboutDialogVisible
import com.github.whyrising.recompose.regSub

fun regHomeSubs() {
  regSub<AppDb, Boolean>(
    queryId = Ids.isDisableServiceDialogVisible,
  ) { db, _ ->
    db.isDisableServiceDialogVisible
  }

  regSub<AppDb, Boolean>(
    queryId = isAboutDialogVisible,
  ) { db, _ ->
    db.isAboutDialogVisible
  }
}
