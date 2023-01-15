package com.github.whyrising.flashyalarm.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.github.whyrising.flashyalarm.R
import com.github.whyrising.flashyalarm.designsystem.theme.Blue300

@Composable
fun FaListItemTitle(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.headlineSmall
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
        bottom = dimensionResource(id = R.dimen.small_50)
      ),
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(
      modifier = Modifier.height(dimensionResource(id = R.dimen.small_100))
    )
  }
}


@Composable
fun AlertDialogBodyTitle(name: String) {
  Text(
    text = name,
    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.small_50)),
    style = MaterialTheme.typography.bodyMedium
  )
}

@Composable
fun AlertDialogBodyText(name: String) {
  Text(
    text = name,
    modifier = Modifier.padding(bottom = dimensionResource(R.dimen.normal_100)),
    style = MaterialTheme.typography.bodyLarge
  )
}

@Composable
fun Hyperlink(url: String) {
  val uriHandler = LocalUriHandler.current
  ClickableText(
    text = buildAnnotatedString {
      append(url)
      addStyle(
        style = SpanStyle(
          color = Blue300,
          fontSize = 16.sp,
          textDecoration = TextDecoration.Underline
        ),
        start = 0,
        end = url.length
      )
    },
    onClick = {
      uriHandler.openUri(url)
    }
  )
}
