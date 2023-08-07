/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.github.andiim.plantscan.app.ui.common.composables

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.R.drawable as AppIcon
import com.github.andiim.plantscan.app.R.string as AppText

@Composable
fun BasicField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
  OutlinedTextField(
      singleLine = true,
      modifier = modifier,
      value = value,
      onValueChange = { onNewValue(it) },
      placeholder = { Text(stringResource(text)) })
}

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
  OutlinedTextField(
      singleLine = true,
      modifier = modifier,
      value = value,
      onValueChange = { onNewValue(it) },
      placeholder = { Text(stringResource(AppText.email)) },
      leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email") })
}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
  PasswordField(value, AppText.password, onNewValue, modifier)
}

@Composable
fun RepeatPasswordField(
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
  PasswordField(value, AppText.repeat_password, onNewValue, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
  var isVisible by remember { mutableStateOf(false) }

  val icon =
      if (isVisible) painterResource(AppIcon.ic_visibility_on)
      else painterResource(AppIcon.ic_visibility_off)

  val visualTransformation =
      if (isVisible) VisualTransformation.None else PasswordVisualTransformation()

  OutlinedTextField(
      modifier = modifier,
      value = value,
      onValueChange = { onNewValue(it) },
      placeholder = { Text(text = stringResource(placeholder)) },
      leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock") },
      trailingIcon = {
        IconButton(onClick = { isVisible = !isVisible }) {
          Icon(painter = icon, contentDescription = "Visibility")
        }
      },
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
      visualTransformation = visualTransformation)
}

@Preview(name = "Night Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Day Mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TextViewPreview() {
  PlantScanTheme {
    Surface {
      Column(
          verticalArrangement = Arrangement.SpaceEvenly,
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.padding(16.dp)) {
        BasicField(text = AppText.app_name, value = "", onNewValue = {})
        Spacer(modifier = Modifier.padding(8.dp))
        EmailField(value = "", onNewValue = {})
        Spacer(modifier = Modifier.padding(8.dp))
        PasswordField(value = "", onNewValue = {})
        Spacer(modifier = Modifier.padding(8.dp))
        RepeatPasswordField(value = "", onNewValue = {})
      }
    }
  }
}
