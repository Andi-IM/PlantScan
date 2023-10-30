package com.github.andiim.plantscan.app.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.github.andiim.plantscan.app.ui.PsAppState
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.camera.cameraScreen
import com.github.andiim.plantscan.feature.camera.navigateToCamera
import com.github.andiim.plantscan.feature.findplant.navigation.FindPlantGraph
import com.github.andiim.plantscan.feature.findplant.navigation.findPlantGraph
import com.github.andiim.plantscan.feature.history.navigation.historyScreen
import com.github.andiim.plantscan.feature.settings.navigation.settingsScreen
import com.github.andiim.plantscan.feature.web.webViewScreen

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive/
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
@Suppress("UNUSED_PARAMETER")
fun PsHost(
    appState: PsAppState,
    onShowSnackbar: suspend (String, String?, SnackbarDuration) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: AppDestination = FindPlantGraph,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier,
    ) {
        findPlantGraph(
            onItemClick = {},
            onCameraClick = navController::navigateToCamera,
            onPlantClick = {},
            nestedGraphs = {
                cameraScreen(onBackClick = navController::popBackStack)
            },
        )
        historyScreen()
        settingsScreen()
        webViewScreen(onBackClick = {})
    }
}
