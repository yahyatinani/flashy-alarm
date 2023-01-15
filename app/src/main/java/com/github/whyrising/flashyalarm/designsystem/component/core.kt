package com.github.whyrising.flashyalarm.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.designsystem.theme.Blue300

@Composable
fun SwitchStyled(
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  colors: ColorScheme = MaterialTheme.colorScheme
) {
  Switch(
    checked = checked,
    onCheckedChange = onCheckedChange,
    colors = SwitchDefaults.colors(
      checkedThumbColor = colors.inversePrimary,
      checkedTrackColor = colors.primary
    )
  )
}

@Composable
fun FaDivider() {
  Divider(
    thickness = 1.dp,
    modifier = Modifier
      .padding(horizontal = dimensionResource(R.dimen.normal_100))
  )
}

@Composable
fun FaColumn(
  content: @Composable ColumnScope.() -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        color = MaterialTheme.colorScheme.background
      ),
    content = content
  )
}

@Composable
fun ConfigSection(content: @Composable () -> Unit) {
  Card(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth()
  ) {
    Column {
      content()
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaListItem(
  modifier: Modifier = Modifier,
  secondaryText: @Composable (() -> Unit)? = null,
  trailing: @Composable (() -> Unit)? = null,
  title: @Composable () -> Unit
) {
  ListItem(
    modifier = modifier
      .padding(start = dimensionResource(R.dimen.small_50)),
    supportingText = secondaryText,
    trailingContent = trailing,
    headlineText = title
  )
}
