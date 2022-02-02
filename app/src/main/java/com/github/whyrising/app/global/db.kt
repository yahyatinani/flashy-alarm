package com.github.whyrising.app.global

import com.github.whyrising.app.home.DbHomeSchema
import com.github.whyrising.app.home.defaultDbHomeSchema

data class DbSchema(
    val screenTitle: String,
    val home: DbHomeSchema,
    val isDark: Boolean = false
)

val defaultDb = DbSchema(
    screenTitle = "Page Title",
    home = defaultDbHomeSchema,
)
