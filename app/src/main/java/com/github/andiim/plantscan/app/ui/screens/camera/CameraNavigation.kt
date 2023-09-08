package com.github.andiim.plantscan.app.ui.screens.camera

import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.databinding.FragmentContainerBinding
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun PlantScanAppState.navigateToCamera() {
    this.navigate(Direction.Camera.route, singleTopLaunch = false)
}


fun NavGraphBuilder.cameraFragment(
    onBackClick: () -> Unit,
    onDetectionClick: (String) -> Unit
) {
    composable(route = Direction.Camera.route) {
        AndroidViewBinding(FragmentContainerBinding::inflate) {
            root.getFragment<CameraFragment>().apply {
                popUpScreen = onBackClick
                toDetect = onDetectionClick
            }
        }
    }
}
