package com.github.whyrising.flashyalarm.home

import com.github.whyrising.flashyalarm.base.AppDb
import com.github.whyrising.flashyalarm.base.base.isAboutDialogVisible
import com.github.whyrising.recompose.regSub

fun regHomeSubs() {
  regSub<AppDb, Boolean>(
    queryId = home.isDisableServiceDialogVisible
  ) { db, _ ->
    db.isDisableServiceDialogVisible
  }

  regSub<AppDb, Boolean>(
    queryId = isAboutDialogVisible
  ) { db, _ ->
    db.isAboutDialogVisible
  }
}
