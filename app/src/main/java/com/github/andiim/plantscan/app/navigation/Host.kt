package com.github.andiim.plantscan.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.github.andiim.plantscan.app.ui.AppState
import com.github.andiim.plantscan.feature.camera.cameraScreen
import com.github.andiim.plantscan.feature.findplant.navigation.FindPlant
import com.github.andiim.plantscan.feature.findplant.navigation.findPlantScreen
import com.github.andiim.plantscan.feature.web.webViewScreen

@Composable
fun Host(state: AppState, modifier: Modifier = Modifier) {
    val navController = state.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = FindPlant.route
    ) {
        findPlantScreen(onItemClick = {}, onCameraClick = {}, onPlantClick = {})
        cameraScreen()
        webViewScreen(onBackClick = {})
    }
}
