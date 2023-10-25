package com.github.andiim.plantscan.app.ui.features.camera

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.features.camera.noPermission.NoPermissionContent
import com.github.andiim.plantscan.app.ui.features.camera.photoCapture.CameraScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

fun NavGraphBuilder.cameraScreen(){
    composable("camera"){
        CameraFeature()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraFeature() {
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        CameraScreen()
    } else {
        NoPermissionContent(onRequestPermission = cameraPermissionState::launchPermissionRequest)
    }
}