package com.github.andiim.plantscan.app.ui.screens.web

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.webViewScreen(appState: PlantScanAppState) {
    composable(
        route = Direction.Web.route,
        arguments = listOf(navArgument("url") { type = NavType.StringType })
    ) { backStackEntry ->
        val url = backStackEntry.arguments?.getString("url")
        url?.let { WebScreen(url = it, name = "Testing", popUpScreen = appState::popUp) }
    }
}
