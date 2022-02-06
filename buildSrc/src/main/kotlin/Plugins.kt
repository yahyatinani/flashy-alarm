object Plugins {
    object Ktlint {
        const val version = "10.2.1"
        const val id = "org.jlleitschuh.gradle.ktlint"
    }

    object Gradle {
        const val android = "com.android.tools.build:gradle:7.1.1"
        const val kotlin =
            "org.jetbrains.kotlin:kotlin-gradle-plugin:${Libs.kotlinVersion}"
    }
}