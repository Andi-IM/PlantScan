package com.github.andiim.plantscan.feature.camera

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.camera.noPermission.NoPermissionScreen
import com.github.andiim.plantscan.feature.camera.photoCapture.CameraScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

fun NavController.navigateToCamera() {
    this.navigate(Camera.route) { launchSingleTop = true }
}

fun NavGraphBuilder.cameraScreen(
    onBackClick: () -> Unit,
) {
    composable(Camera.route) {
        CameraFeature(
            onBackClick = onBackClick,
        )
    }
}

object Camera : AppDestination {
    override val route: String = "camera"
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CameraFeature(
    onBackClick: () -> Unit,
) {
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        TrackScreenViewEvent(screenName = "Camera")
        CameraScreen(onBackClick = onBackClick)
    } else {
        TrackScreenViewEvent(screenName = "Blocked Camera")
        NoPermissionScreen(
            onBackClick = onBackClick,
            onRequestPermission = cameraPermissionState::launchPermissionRequest,
        )
    }
}
