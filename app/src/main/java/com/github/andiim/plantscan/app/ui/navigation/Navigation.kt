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
                routeToWeb = appState::navigateToWeb,
                navigateAndPopUp = appState::navigateFromAuthRoute,
            )
            webViewScreen(onBackPressed = appState::popUp)
        }

        detailScreen(onBackPressed = appState::popUp)

        navigation(startDestination = Direction.FindPlant.route, route = Direction.MainNav.route) {
            homeFindPlantElement(
                routeToDetail = appState::navigateToDetail,
                routeToCamera = appState::navigateToCamera,
                routeToList = appState::navigateToList
            )
            homeHistoryElement()
            homeSettingsElement(
                clearAndNavigate = appState::clearAndNavigate,
                routeToLogin = appState::navigateToAuthRoute,
            )
        }

        listScreen(
            onBackPressed = appState::popUp,
            routeToDetail = appState::navigateToDetail,
        )
        splashScreen(navigateAndPopUp = appState::gotoMainNavRoute)

        cameraFragment(
            onBackPressed = appState::popUp,
            routeToDetect = appState::navigateToDetect
        )
        detectFragment()
    }
}




