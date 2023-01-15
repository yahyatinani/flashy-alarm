package com.github.whyrising.flashyalarm.alarmlistenerservice.torchcontrol

interface TorchController {
  fun off()
}

interface StaticPattern : TorchController {
  fun on()
}

interface DynamicPattern : TorchController {
  fun on(frequency: Long)
}
