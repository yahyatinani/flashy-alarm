package com.github.whyrising.flashyalarm

import java.io.File
import java.io.FileOutputStream
import java.util.Base64

object Build {
  const val APP_ID = "com.github.whyrising.flashyalarm"
  const val COMPOSE_VERSION = "1.2.0-rc02"

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
}
