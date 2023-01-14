package com.github.whyrising.flashyalarm.base

@Suppress("EnumEntryName")
enum class base {
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
