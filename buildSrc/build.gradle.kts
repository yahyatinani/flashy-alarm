plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  google()
  gradlePluginPortal()
}

dependencies {
  implementation(deps.kotlin.android.plugin)
  implementation(deps.android.application.gradle.plugin)
}
