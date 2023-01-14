package com.github.whyrising.flashyalarm.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black

private val LightColorPalette = lightColorScheme(
  primary = Yellow500,
  inversePrimary = Yellow700,
  onPrimary = Black,
  secondary = Black
)

private val DarkColorPalette = darkColorScheme(
  primary = Yellow200,
  inversePrimary = Yellow500,
  secondary = Yellow200
)

@Composable
fun FlashyAlarmTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}
