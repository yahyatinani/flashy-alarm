package com.github.whyrising.flashyalarm.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black

private val LightColorPalette = lightColors(
  primary = Yellow500,
  primaryVariant = Yellow700,
  onPrimary = Black,
  secondary = Black
)

private val DarkColorPalette = darkColors(
  primary = Yellow200,
  primaryVariant = Yellow500,
  secondary = Yellow200
)

@Composable
fun FlashyAlarmTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colors = if (darkTheme) DarkColorPalette else LightColorPalette,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}

// -- Composables --------------------------------------------------------------

@Composable
fun SwitchStyled(
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  colors: Colors = MaterialTheme.colors
) {
  Switch(
    checked = checked,
    onCheckedChange = onCheckedChange,
    colors = SwitchDefaults.colors(
      checkedThumbColor = colors.primaryVariant,
      checkedTrackColor = colors.primary,
    )
  )
}
