package com.github.andiim.orchidscan.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.andiim.orchidscan.app.ui.screens.splash.SplashScreen
import com.github.andiim.orchidscan.app.ui.screens.web.WebScreen
import com.github.andiim.orchidscan.app.ui.states.PlantScanAppState

@Composable
fun SetupRootNavGraph(appState: PlantScanAppState, modifier: Modifier = Modifier) {
    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = Direction.Splash.route,
    ) {
        addSplashScreen(appState)

        navigation(
            startDestination = Direction.SearchOrDetect.route,
            route = Direction.MainNav.route
        ) {
            // TODO : Add Bottom Navigation
        }

        navigation(startDestination = Direction.Login.route, route = Direction.AccountNav.route) {
            addWebViewScreen(appState)
            // TODO : Something indirection
        }

    }
}

private fun NavGraphBuilder.addWebViewScreen(appState: PlantScanAppState) {
    composable(
        route = Direction.WebScreenView.route,
        arguments = listOf(navArgument("url") { type = NavType.StringType })
    ) { backStackEntry ->
        val url = backStackEntry.arguments?.getString("url")
        url?.let { WebScreen(url = it, name = "Testing", popUpScreen = appState::popUp) }
    }
}

private fun NavGraphBuilder.addSplashScreen(appState: PlantScanAppState) {
    composable(route = Direction.Splash.route) {
        SplashScreen(openAndPopUp = appState::navigateAndPopUp)
    }
}


