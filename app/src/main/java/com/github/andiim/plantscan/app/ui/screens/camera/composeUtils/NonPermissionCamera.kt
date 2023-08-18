package com.github.andiim.plantscan.app.ui.screens.camera.composeUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.github.andiim.plantscan.app.R.string as AppText

@Composable
fun NonPermissionScreen(shouldShowRationale: Boolean, onClick: () -> Unit = {}) {

  val stringResource =
      if (shouldShowRationale) R.string.camera_permissions_request_with_rationale
      else R.string.camera_permissions_request

  Column(
      modifier = Modifier.fillMaxSize().background(color = Color.Black).padding(32.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            stringResource(stringResource),
            style = (MaterialTheme.typography).bodyMedium,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp))
        Button(onClick = onClick) { Text(stringResource(AppText.camera_permissions_action)) }
      }
}

@Preview
@Composable
fun Preview_NonPermission() {
  PlantScanTheme { NonPermissionScreen(false) }
}
