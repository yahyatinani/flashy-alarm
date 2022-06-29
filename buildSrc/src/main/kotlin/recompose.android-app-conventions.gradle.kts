import com.github.whyrising.flashyalarm.Build.APP_ID
import com.github.whyrising.flashyalarm.Build.COMPOSE_VERSION
import com.github.whyrising.flashyalarm.Build.keyStoreBase64ToStoreFile

plugins {
  id("kotlin-conventions")
  id("com.android.application")
//  id("recompose.publishing-conventions")
}

android {
  compileSdk = 32

  defaultConfig {
    applicationId = APP_ID
    minSdk = 22
    targetSdk = 32
    versionCode = 1
    versionName = "0.0.1"
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
      if (System.getenv("SIGNING_KEY_BASE64") != null)
        signingConfig = signingConfigs.getByName("debug")
    }
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
      signingConfig = signingConfigs.getByName("release")
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
