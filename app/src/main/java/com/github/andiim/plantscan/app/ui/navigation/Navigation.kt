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
import com.github.andiim.plantscan.app.ui.screens.home.findPlant.gotoMainNavRoute
import com.github.andiim.plantscan.app.ui.screens.home.findPlant.homeFindPlantElement
import com.github.andiim.plantscan.app.ui.screens.home.history.homeHistoryElement
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
    NavHost(
        modifier = modifier.semantics(false) { contentDescription = "Nav Host" },
        navController = appState.navController,
        startDestination = Direction.Splash.route,
    ) {
        navigation(startDestination = Direction.Login.route, route = Direction.AccountNav.route) {
            authScreen(
                onLinkClick = appState::navigateToWeb,
                onAuthClick = appState::navigateFromAuthRoute,
            )
            webViewScreen(onBackClick = appState::popUp)
        }

        detailScreen(onBackClick = appState::popUp)

        navigation(startDestination = Direction.FindPlant.route, route = Direction.MainNav.route) {
            homeFindPlantElement(
                onItemClick = appState::navigateToDetail,
                onCameraClick = appState::navigateToCamera,
                onListClick = appState::navigateToList
            )
            homeHistoryElement()
            homeSettingsElement(
                clearAndNavigate = appState::clearAndNavigate,
                onLoginClick = appState::navigateToAuthRoute,
            )
        }

        listScreen(
            onBackClick = appState::popUp,
            onDetailClick = appState::navigateToDetail,
        )
        splashScreen(onLoadingFinished = appState::gotoMainNavRoute)

        cameraFragment(
            onBackClick = appState::popUp,
            onDetectionClick = appState::navigateToDetect
        )
        detectFragment(
            backToTop = {
                appState.navigateAndPopUp(
                    Direction.MainNav.route,
                    Direction.Detect.route
                )
            },
            onSuggestClick = appState::navigateToSuggest
        )

        suggestScreen(
            onBackClick = appState::popUp,
            onAuthClick = appState::navigateToAuthRoute
        )
    }
}
