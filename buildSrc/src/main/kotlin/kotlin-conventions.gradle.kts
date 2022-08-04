import com.github.whyrising.flashyalarm.Build
import com.github.whyrising.flashyalarm.Build.Versions.KOTLIN
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("android")
}

tasks.withType<Test> {
  useJUnitPlatform()
  val decimal = Runtime.getRuntime().availableProcessors() / 2
  maxParallelForks = if (decimal > 0) decimal else 1
  filter {
    isFailOnNoMatchingTests = false
  }
  testLogging {
    exceptionFormat = TestExceptionFormat.FULL
    events = setOf(
      TestLogEvent.SKIPPED,
      TestLogEvent.FAILED,
      TestLogEvent.STANDARD_OUT,
      TestLogEvent.STANDARD_ERROR
    )
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    jvmTarget = Build.Versions.JVM
    apiVersion = KOTLIN
    languageVersion = KOTLIN
  }
}

kotlin {
  sourceSets {
    all {
      languageSettings.optIn("kotlin.time.ExperimentalTime")
      languageSettings.optIn("kotlin.experimental.ExperimentalTypeInference")
      languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
    }
  }
}
