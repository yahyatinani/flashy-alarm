package com.github.whyrising.flashyalarm.panel.common

@Suppress("EnumEntryName")
enum class common {
  // Events
  initAppDb,
  updateScreenTitle,
  navigate,
  exitApp,
  pushNotification,
  isAboutDialogVisible,
  setBackstackStatus,
  goBack,

  // Subs
  screenTitle,
  formatScreenTitle,
  isBackstackAvailable,

  // Fx
  navigateFx
}
