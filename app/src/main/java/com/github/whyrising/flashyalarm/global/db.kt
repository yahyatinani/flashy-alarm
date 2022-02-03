package com.github.whyrising.flashyalarm.global

import com.github.whyrising.flashyalarm.home.DbHomeSchema
import com.github.whyrising.flashyalarm.home.defaultDbHomeSchema

data class DbSchema(
    val screenTitle: String,
    val home: DbHomeSchema,
    val isDark: Boolean = false
)

val defaultDb = DbSchema(
    screenTitle = "Page Title",
    home = defaultDbHomeSchema,
)
