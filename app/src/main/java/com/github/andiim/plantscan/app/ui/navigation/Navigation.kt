package com.github.andiim.plantscan.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.screens.auth.authScreen
import com.github.andiim.plantscan.app.ui.screens.auth.navigateFromAuthRoute
import com.github.andiim.plantscan.app.ui.screens.auth.navigateToAuthRoute
import com.github.andiim.plantscan.app.ui.screens.camera.cameraFragment
import com.github.andiim.plantscan.app.ui.screens.camera.navigateToCamera
import com.github.andiim.plantscan.app.ui.screens.detail.detailScreen
import com.github.andiim.plantscan.app.ui.screens.detail.navigateToDetail
import com.github.andiim.plantscan.app.ui.screens.detect.detectFragment
import com.github.andiim.plantscan.app.ui.screens.detect.navigateToDetect
import com.github.andiim.plantscan.app.ui.screens.home.findPlant.homeFindPlantElement
import com.github.andiim.plantscan.app.ui.screens.home.findPlant.navigateToMainNavAndPopUpDetect
import com.github.andiim.plantscan.app.ui.screens.home.findPlant.navigateToMainNavAndPopUpSplash
import com.github.andiim.plantscan.app.ui.screens.home.history.homeHistoryElement
import com.github.andiim.plantscan.app.ui.screens.home.settings.clearAndNavigateFromSettings
import com.github.andiim.plantscan.app.ui.screens.home.settings.homeSettingsElement
import com.github.andiim.plantscan.app.ui.screens.list.listScreen
import com.github.andiim.plantscan.app.ui.screens.list.navigateToList
import com.github.andiim.plantscan.app.ui.screens.splash.splashScreen
import com.github.andiim.plantscan.app.ui.screens.suggest.navigateToSuggest
import com.github.andiim.plantscan.app.ui.screens.suggest.suggestScreen
import com.github.andiim.plantscan.app.ui.screens.web.navigateToWeb
import com.github.andiim.plantscan.app.ui.screens.web.webViewScreen

@Composable
fun SetupRootNavGraph(appState: PlantScanAppState, modifier: Modifier = Modifier) {
    val navController = appState.navController
    NavHost(
        modifier = modifier.semantics(false) { contentDescription = "Nav Host" },
        navController = navController,
        startDestination = Direction.Splash.route,
    ) {
        navigation(startDestination = Direction.Login.route, route = Direction.AccountNav.route) {
            authScreen(
                onLinkClick = navController::navigateToWeb,
                onAuthClick = navController::navigateFromAuthRoute,
            )
            webViewScreen(onBackClick = navController::popBackStack)
        }

        detailScreen(onBackClick = navController::popBackStack)

        navigation(startDestination = Direction.FindPlant.route, route = Direction.MainNav.route) {
            homeFindPlantElement(
                onItemClick = navController::navigateToDetail,
                onCameraClick = navController::navigateToCamera,
                onListClick = navController::navigateToList
            )
            homeHistoryElement {}
            homeSettingsElement(
                clearAndNavigate = navController::clearAndNavigateFromSettings,
                onLoginClick = navController::navigateToAuthRoute,
            )
        }

        listScreen(
            onBackClick = navController::popBackStack,
            onDetailClick = navController::navigateToDetail,
        )
        splashScreen(onLoadingFinished = navController::navigateToMainNavAndPopUpSplash)

        cameraFragment(
            onBackClick = navController::popBackStack,
            onDetectionClick = navController::navigateToDetect
        )
        detectFragment(
            backToTop = navController::navigateToMainNavAndPopUpDetect,
            onSuggestClick = navController::navigateToSuggest
        )

        suggestScreen(
            onBackClick = navController::popBackStack,
            onAuthClick = navController::navigateToAuthRoute
        )
    }
}
