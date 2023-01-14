package com.github.whyrising.flashyalarm.panel.common

@Suppress("EnumEntryName")
enum class common {
  // Events
  initAppDb,
  updateScreenTitle,
  navigate,
  exitApp,
  isAboutDialogVisible,
  setBackstackStatus,
  goBack,
  phoneHasTorch,
  checkDeviceHasTorch,

  // Subs
  screenTitle,
  formatScreenTitle,
  isBackstackAvailable,

  // Fx
  navigateFx,
  isAlarmListenerRunning
}
