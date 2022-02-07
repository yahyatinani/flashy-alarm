package com.github.whyrising.flashyalarm.base

data class DbSchema(
    val screenTitle: String,
    val isNotifAccessEnabled: Boolean = false,
)

val defaultDb = DbSchema(screenTitle = "")
