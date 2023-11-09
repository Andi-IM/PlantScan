package com.github.andiim.plantscan.app.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.github.andiim.plantscan.app.ui.PsAppState
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.account.navigation.authScreen
import com.github.andiim.plantscan.feature.account.navigation.navigateToAuth
import com.github.andiim.plantscan.feature.camera.navigation.cameraScreen
import com.github.andiim.plantscan.feature.camera.navigation.navigateToCamera
import com.github.andiim.plantscan.feature.detect.detail.navigation.detectDetailScreen
import com.github.andiim.plantscan.feature.detect.detail.navigation.navigateToDetectionDetail
import com.github.andiim.plantscan.feature.detect.navigation.detectScreen
import com.github.andiim.plantscan.feature.detect.navigation.navigateToDetection
import com.github.andiim.plantscan.feature.findplant.navigation.FindPlantGraph
import com.github.andiim.plantscan.feature.findplant.navigation.findPlantGraph
import com.github.andiim.plantscan.feature.history.navigation.historyScreen
import com.github.andiim.plantscan.feature.plant.navigation.navigateToPlants
import com.github.andiim.plantscan.feature.plant.navigation.plantScreen
import com.github.andiim.plantscan.feature.plantdetail.navigation.navigateToPlantDetail
import com.github.andiim.plantscan.feature.plantdetail.navigation.plantDetailScreen
import com.github.andiim.plantscan.feature.settings.navigation.clearAndNavigateSettings
import com.github.andiim.plantscan.feature.settings.navigation.settingsGraph
import com.github.andiim.plantscan.feature.suggest.navigation.navigateToSuggest
import com.github.andiim.plantscan.feature.suggest.navigation.suggestScreen

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive/
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun PsHost(
    appState: PsAppState,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
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
            onItemClick = navController::navigateToPlantDetail,
            onCameraClick = navController::navigateToCamera,
            onPlantsClick = navController::navigateToPlants,
            nestedGraphs = {
                cameraScreen(
                    onBackClick = navController::popBackStack,
                    onShowSnackbar = onShowSnackbar,
                    onImageCaptured = navController::navigateToDetection,
                )
                plantScreen(
                    onBackClick = navController::popBackStack,
                    onPlantClick = navController::navigateToPlantDetail,
                )
            },
        )

        plantDetailScreen(
            onBackClick = navController::popBackStack,
        )

        detectScreen(
            onBackClick = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            onSuggestClick = navController::navigateToSuggest,
        )

        detectDetailScreen(
            onBackClick = navController::popBackStack,
            onSuggestClick = navController::navigateToSuggest
        )

        suggestScreen(
            onBackClick = navController::popBackStack,
            onShowSnackbar = onShowSnackbar,
            onLoginPressed = navController::navigateToAuth,
        )

        historyScreen(
            onDetailClick = navController::navigateToDetectionDetail,
        )

        settingsGraph(
            onLoginClick = navController::navigateToAuth,
            nestedGraphs = {
                authScreen(
                    onBackPressed = navController::popBackStack,
                    authCallback = navController::clearAndNavigateSettings,
                    onShowSnackbar = onShowSnackbar,
                )
            },
        )
    }
}
