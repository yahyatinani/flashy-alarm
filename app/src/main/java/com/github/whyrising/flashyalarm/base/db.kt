package com.github.whyrising.flashyalarm.base

data class DbSchema(
    val screenTitle: String,
    val isNotifAccessEnabled: Boolean = false,
    val showAlertDialog: Boolean = false,
    val isFlashSupported: Boolean = false,
)

val defaultDb = DbSchema(screenTitle = "")
