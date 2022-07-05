package com.github.whyrising.flashyalarm.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.Colors
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.github.whyrising.flashyalarm.R

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

@Composable
fun SectionTitle(text: String) {
  Column {
    Text(
      text = text,
      modifier = Modifier.padding(
        top = dimensionResource(id = R.dimen.normal_100),
        start = dimensionResource(id = R.dimen.normal_125),
        bottom = dimensionResource(id = R.dimen.small_50),
      ),
      style = MaterialTheme.typography.overline
    )
    Spacer(
      modifier = Modifier.height(dimensionResource(id = R.dimen.small_100))
    )
  }
}

@Composable
fun ConfigDivider() {
  Divider(
    thickness = 1.dp,
    modifier = Modifier
      .padding(horizontal = dimensionResource(R.dimen.normal_100))
  )
}

@Composable
fun ConfigColumn(
  content: @Composable ColumnScope.() -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colors.onSurface.copy(alpha = .03f)),
    content = content
  )
}

@Composable
fun ConfigSection(content: @Composable () -> Unit) {
  Card(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth(),
  ) {
    Column {
      content()
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ConfigItem(
  modifier: Modifier = Modifier,
  secondaryText: @Composable (() -> Unit)? = null,
  trailing: @Composable (() -> Unit)? = null,
  title: @Composable () -> Unit
) {
  ListItem(
    modifier = modifier
      .padding(start = dimensionResource(R.dimen.small_50)),
    secondaryText = secondaryText,
    trailing = trailing,
    text = title
  )
}
