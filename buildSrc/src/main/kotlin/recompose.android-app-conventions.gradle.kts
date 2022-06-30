import com.github.whyrising.flashyalarm.Build.APP_ID
import com.github.whyrising.flashyalarm.Build.COMPOSE_VERSION
import com.github.whyrising.flashyalarm.Build.keyStoreBase64ToStoreFile
import com.github.whyrising.flashyalarm.Build.versionMajor
import com.github.whyrising.flashyalarm.Build.versionMinor
import com.github.whyrising.flashyalarm.Build.versionPatch

plugins {
  id("kotlin-conventions")
  id("com.android.application")
}

android {
  compileSdk = 32

  defaultConfig {
    applicationId = APP_ID
    minSdk = 22
    targetSdk = 32
    versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
    versionName = "$versionMajor.$versionMinor.$versionPatch"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  signingConfigs {
    create("release") {
      storeFile = keyStoreBase64ToStoreFile(System.getenv("SIGNING_KEY_BASE64"))
      storePassword = System.getenv("KEYSTORE_PASSWORD")
      keyAlias = System.getenv("KEY_ALIAS")
      keyPassword = System.getenv("KEY_PASSWORD")
    }
  }

  buildTypes {
    debug {
      isMinifyEnabled = false
      versionNameSuffix = "-debug"
      resValue(
        type = "string",
        name = "app_version",
        value = "${defaultConfig.versionName}$versionNameSuffix"
      )
      if (System.getenv("SIGNING_KEY_BASE64") != null)
        signingConfig = signingConfigs.getByName("debug")
    }
    release {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      signingConfig = signingConfigs.getByName("release")
      resValue(
        type = "string",
        name = "app_version",
        value = "${defaultConfig.versionName}"
      )
    }
  }

  packagingOptions {
    resources {
      excludes += setOf("/*.jar", "/META-INF/{AL2.0,LGPL2.1}")
    }
  }

  buildFeatures {
    compose = true
  }


  composeOptions {
    kotlinCompilerExtensionVersion = COMPOSE_VERSION
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  testOptions {
    animationsDisabled = true
    unitTests.isReturnDefaultValues = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = COMPOSE_VERSION
  }

  publishing {
    singleVariant("release") {
      withSourcesJar()
      withJavadocJar()
    }
  }
}
