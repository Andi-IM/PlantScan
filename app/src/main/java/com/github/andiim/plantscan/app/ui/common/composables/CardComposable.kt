package com.github.andiim.plantscan.app.ui.common.composables

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.app.ui.common.extensions.dropdownSelector
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.R.drawable as AppDrawable
import com.github.andiim.plantscan.app.R.string as AppText

@Composable
fun DangerousCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
  CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.primary, modifier)
}

@Composable
fun RegularCardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    modifier: Modifier,
    onEditClick: () -> Unit
) {
  CardEditor(title, icon, content, onEditClick, MaterialTheme.colorScheme.onSurface, modifier)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CardEditor(
    @StringRes title: Int,
    @DrawableRes icon: Int,
    content: String,
    onEditClick: () -> Unit,
    highlightColor: Color,
    modifier: Modifier
) {
  Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
      modifier = modifier,
      onClick = onEditClick) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(16.dp)) {
              Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(title), color = highlightColor)
              }

              if (content.isNotBlank()) {
                Text(text = content, modifier = Modifier.padding(16.dp, 0.dp))
              }

              Icon(
                  painter = painterResource(icon),
                  contentDescription = "Icon",
                  tint = highlightColor)
            }
      }
}

@Composable
fun CardSelector(
    @StringRes label: Int,
    options: List<String>,
    selection: String,
    modifier: Modifier,
    onNewValue: (String) -> Unit
) {
  Card(
      colors =
          CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
          ),
      modifier = modifier) {
        DropdownSelector(
            label = label,
            options = options,
            selection = selection,
            modifier = Modifier.dropdownSelector(),
            onNewValue = onNewValue)
      }
}

@Preview(name = "Night Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Day Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun Preview() {
  PlantScanTheme {
    Surface {
      Column(modifier = Modifier.padding(16.dp)) {
        DangerousCardEditor(
            title = AppText.app_name,
            icon = AppDrawable.ic_visibility_on,
            content = "something",
            modifier = Modifier,
            onEditClick = {})
        Spacer(Modifier.padding(8.dp))
        RegularCardEditor(
            title = AppText.app_name,
            icon = AppDrawable.ic_visibility_on,
            content = "something",
            modifier = Modifier,
            onEditClick = {})
        Spacer(Modifier.padding(8.dp))
        CardSelector(
            label = AppText.app_name,
            options = listOf("Hello", "World!"),
            selection = "select this?",
            modifier = Modifier,
            onNewValue = {})
      }
    }
  }
}
