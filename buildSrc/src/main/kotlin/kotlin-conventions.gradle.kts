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
    jvmTarget = "1.8"
    apiVersion = "1.6"
    languageVersion = "1.6"
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
