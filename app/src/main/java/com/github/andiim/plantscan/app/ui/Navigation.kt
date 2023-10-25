package com.github.andiim.plantscan.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.andiim.plantscan.app.ui.features.camera.CameraFeature
import com.github.andiim.plantscan.app.ui.features.camera.cameraScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Camera.route) {
        cameraScreen()
    }
}