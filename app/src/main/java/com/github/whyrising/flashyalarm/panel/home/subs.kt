package com.github.whyrising.flashyalarm.panel.home

import com.github.whyrising.flashyalarm.panel.common.AppDb
import com.github.whyrising.flashyalarm.panel.common.common.isAboutDialogVisible
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
