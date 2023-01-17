import Build.APP_ID
import Build.Versions.COMPOSE_COMPILER
import Build.keyStoreBase64ToStoreFile
import Build.versionMajor
import Build.versionMinor
import Build.versionPatch
import org.gradle.api.JavaVersion.VERSION_1_8

plugins {
  id("kotlin-conventions")
  id("com.android.application")
}

android {
  compileSdk = 33

  defaultConfig {
    applicationId = APP_ID
    minSdk = 23
    targetSdk = 33
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
    kotlinCompilerExtensionVersion = COMPOSE_COMPILER
  }

  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = VERSION_1_8
    targetCompatibility = VERSION_1_8
  }

  testOptions {
    animationsDisabled = true
    unitTests.isReturnDefaultValues = true
  }

  publishing {
    singleVariant("release") {
      withSourcesJar()
      withJavadocJar()
    }
  }
}
