package com.github.whyrising.flashyalarm.flashpattern

enum class LightPattern {
  STATIC,
  BLINK;

  companion object {
    fun patternBy(name: String) = valueOf(name.uppercase())
  }
}
