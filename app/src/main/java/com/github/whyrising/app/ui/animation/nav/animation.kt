package com.github.whyrising.app.ui.animation.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

@ExperimentalAnimationApi
fun exitAnimation(
    targetOffsetX: Int,
    duration: Int = 300
) = slideOutHorizontally(
    targetOffsetX = { targetOffsetX },
    animationSpec = tween(duration)
) + fadeOut(animationSpec = tween(duration))

@ExperimentalAnimationApi
fun enterAnimation(
    initialOffsetX: Int,
    duration: Int = 300
) = slideInHorizontally(
    initialOffsetX = { initialOffsetX },
    animationSpec = tween(duration)
) + fadeIn(animationSpec = tween(duration))
