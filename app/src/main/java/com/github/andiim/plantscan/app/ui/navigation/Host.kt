package com.github.andiim.plantscan.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.github.andiim.plantscan.app.ui.AppState
import com.github.andiim.feature.camera.cameraScreen

@Composable
fun Host(state: AppState, modifier: Modifier = Modifier) {
    val navController = state.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Camera.route) {
        cameraScreen()
    }
}