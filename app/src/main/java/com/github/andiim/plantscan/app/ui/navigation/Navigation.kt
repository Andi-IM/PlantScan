package com.github.andiim.plantscan.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.screens.auth.login.authLoginScreen
import com.github.andiim.plantscan.app.ui.screens.auth.signUp.authSignUpScreen
import com.github.andiim.plantscan.app.ui.screens.camera.cameraFragment
import com.github.andiim.plantscan.app.ui.screens.detail.detailScreen
import com.github.andiim.plantscan.app.ui.screens.detail.navigateToDetail
import com.github.andiim.plantscan.app.ui.screens.detect.detectFragment
import com.github.andiim.plantscan.app.ui.screens.home.findPlant.homeFindPlantElement
import com.github.andiim.plantscan.app.ui.screens.home.myGarden.homeHistoryElement
import com.github.andiim.plantscan.app.ui.screens.home.settings.homeSettingsElement
import com.github.andiim.plantscan.app.ui.screens.list.listScreen
import com.github.andiim.plantscan.app.ui.screens.splash.splashScreen
import com.github.andiim.plantscan.app.ui.screens.web.webViewScreen

@Composable
fun SetupRootNavGraph(appState: PlantScanAppState, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier.semantics(false) { contentDescription = "Nav Host" },
        navController = appState.navController,
        startDestination = Direction.Splash.route,
    ) {
        navigation(startDestination = Direction.Login.route, route = Direction.AccountNav.route) {
            authLoginScreen(appState)
            authSignUpScreen(appState)
            webViewScreen(appState)
        }

        detailScreen(appState)

        navigation(startDestination = Direction.FindPlant.route, route = Direction.MainNav.route) {
            homeFindPlantElement(routeToDetail = appState::navigateToDetail, appState)
            homeHistoryElement(appState)
            homeSettingsElement(appState)
        }

        listScreen(appState)
        splashScreen(navigateAndPopUp = appState::gotoMainNavRoute)

        cameraFragment(appState)
        detectFragment(appState)
    }
}

fun PlantScanAppState.gotoMainNavRoute() {
    this.navigateAndPopUp(Direction.MainNav.route, Direction.Splash.route)
}



