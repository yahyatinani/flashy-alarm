package com.github.whyrising.flashyalarm.global

data class DbSchema(
    val screenTitle: String,
    val isDark: Boolean = false,
    val isNotifAccessEnabled: Boolean = false,
)

val defaultDb = DbSchema(screenTitle = "")
