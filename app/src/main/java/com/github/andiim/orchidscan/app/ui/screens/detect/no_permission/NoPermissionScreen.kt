package com.github.andiim.orchidscan.app.ui.screens.detect.no_permission

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NoPermissionScreen(onRequestPermission: () -> Unit) {
  NoPermissionContent(onRequestPermission)
}

@Composable
fun NoPermissionContent(onRequestPermission: () -> Unit) {
  Column() {
    Text(
        text =
            "Please grant the permission to use the camera to use the core functionality of this app.")
    Button(onClick = onRequestPermission) {
      Icon(Icons.Default.Camera, contentDescription = "Camera")
      Text(text = "Grant permission.")
    }
  }
}

@Preview
@Composable
private fun Preview_NoPermissionContent() {
  NoPermissionContent(onRequestPermission = {})
}
