package com.github.andiim.plantscan.feature.camera.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.camera.photoCapture.CameraRoute

fun NavController.navigateToCamera() {
    this.navigate(Camera.route){
        launchSingleTop = true
    }
}

fun NavGraphBuilder.cameraScreen(
    onBackClick: () -> Unit,
    onImageCaptured: (String) -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
) {
    composable(Camera.route) {
        CameraRoute(
            onBackClick = onBackClick,
            onImageCaptured = onImageCaptured,
            onShowSnackbar = onShowSnackbar,
        )
    }
}

object Camera : AppDestination {
    override val route: String = "camera"
}
