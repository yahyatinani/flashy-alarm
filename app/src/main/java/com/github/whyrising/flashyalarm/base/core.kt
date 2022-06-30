package com.github.whyrising.flashyalarm.base

fun <K> init(context: android.content.Context, clazz: Class<K>) {
  regBaseFx(context, clazz)
  regBaseEvents()
  regBaseSubs()
}
