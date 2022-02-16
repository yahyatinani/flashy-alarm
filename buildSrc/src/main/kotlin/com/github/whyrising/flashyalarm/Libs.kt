package com.github.whyrising.flashyalarm

object Libs {
    const val kotlinVersion = "1.6.10"
    const val jvmTarget = "11"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"

    object Compose {
        const val version = "1.1.0"

        const val ui = "androidx.compose.ui:ui:$version"

        // Tooling support (Previews, etc.)
        const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
        const val uiToolingPreview =
            "androidx.compose.ui:ui-tooling-preview:$version"

        // Foundation (Border, Background, Box, Image, shapes, animations, etc.)
        const val foundation = "androidx.compose.foundation:foundation:$version"

        // Material design
        const val material = "androidx.compose.material:material:$version"

        // Material design icons
        const val iconsCore =
            "androidx.compose.material:material-icons-core:$version"
        const val iconsExt =
            "androidx.compose.material:material-icons-extended:$version"

        const val constraintLayoutCompose =
            "androidx.constraintlayout:constraintlayout-compose:1.0.0"

        const val runtime = "androidx.compose.runtime:runtime:$version"

        // UI Testing
        const val uiTesting = "androidx.compose.ui:ui-test-junit4:$version"
    }

    object Lifecycle {
        private const val version = "2.4.1"
        private const val gr = "androidx.lifecycle"

        // ViewModel
        const val vm = "$gr:lifecycle-viewmodel-ktx:$version"

        // ViewModel utilities for Compose
        const val vmCompose = "$gr:lifecycle-viewmodel-compose:$version"

        // Lifecycles only (without ViewModel or LiveData)
        const val runtime = "$gr:lifecycle-runtime-ktx:$version"

        // Saved state module for ViewModel
        const val savedState = "$gr:lifecycle-viewmodel-savedstate:$version"
    }

    object Activity {
        private const val version = "1.4.0"

        // Integration with activities
        const val compose = "androidx.activity:activity-compose:$version"
    }

    object Navigation {
        private const val version = "2.4.1"

        const val compose = "androidx.navigation:navigation-compose:$version"
    }

    object Androidx {
        // Appcompat is needed for themes.xml resource
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"

        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        const val splash =
            "androidx.core:core-splashscreen:1.0.0-beta01"
    }

    object Accompanist {
        private const val version = "0.24.2-alpha"
        const val navAnimation =
            "com.google.accompanist:accompanist-navigation-animation:$version"

        const val systemUi =
            "com.google.accompanist:accompanist-systemuicontroller:$version"
    }

    object Kotest {
        private const val version = "5.1.0"

        const val runner = "io.kotest:kotest-runner-junit5-jvm:$version"
        const val assertions = "io.kotest:kotest-assertions-core-jvm:$version"
        const val property = "io.kotest:kotest-property-jvm:$version"
    }

    object Y {
        private const val group = "com.github.whyrising.y"
        private const val version = "0.0.10"

        const val core = "$group:y-core:$version"
        const val collections = "$group:y-collections:$version"
        const val concurrency = "$group:y-concurrency:$version"
    }

    object Recompose {
        private const val v = "0.0.6"
        const val recompose = "com.github.whyrising.recompose:recompose:$v"
    }

    object Coroutines {
        private const val group = "org.jetbrains.kotlinx"
        private const val version = "1.5.2"

        const val core = "$group:kotlinx-coroutines-core:$version"
        const val android = "$group:kotlinx-coroutines-android:$version"
        const val coroutinesTest = "$group:kotlinx-coroutines-test:$version"
    }
}
