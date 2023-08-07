package com.github.andiim.orchidscan.app.ui.screens.detect

import android.Manifest
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.andiim.orchidscan.app.ui.screens.detect.no_permission.NoPermissionScreen
import com.github.andiim.orchidscan.app.ui.screens.orchidDetect.CameraScreen
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DetectScreen(
    popUpScreen: () -> Unit,
    viewModel: DetectViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    DetectContent(
        hasPermission = cameraPermissionState.status.isGranted,
        cameraPermissionState::launchPermissionRequest
    )
}

@Composable
fun DetectContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
) {
    if (hasPermission) {
        CameraScreen()
    } else {
        NoPermissionScreen(onRequestPermission)
    }
}

@Preview
@Composable
private fun Preview_DetectContent() {
    PlantScanTheme { Surface { DetectContent(hasPermission = false, onRequestPermission = {}) } }
}