package com.github.whyrising.flashyalarm

import java.io.File
import java.io.FileOutputStream
import java.util.Base64

object Build {
  const val APP_ID = "com.github.whyrising.flashyalarm"

  const val versionMajor = 0
  const val versionMinor = 0
  const val versionPatch = 1

  fun keyStoreBase64ToStoreFile(keyStoreBase64: String?): File? {
    if (keyStoreBase64 == null) return null

    val tempKeyStoreFile = File.createTempFile("tmp_ks_", ".jks")
    var fos: FileOutputStream? = null
    try {
      fos = FileOutputStream(tempKeyStoreFile)
      fos.write(Base64.getDecoder().decode(keyStoreBase64))
      fos.flush()
    } finally {
      fos?.close()
    }

    return tempKeyStoreFile
  }

  object Versions {
    const val COMPOSE_COMPILER = "1.2.0"
    const val KOTLIN = "1.7"
    const val JVM = "1.8"
  }
}
