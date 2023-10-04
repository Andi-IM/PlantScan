package com.github.andiim.plantscan.app.ui.screens.home.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavController.clearAndNavigateFromSettings(route: String) {
    this.navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            // saveState = true
            inclusive = true
        }
        launchSingleTop = true
        // restoreState = true
    }
}

fun NavGraphBuilder.homeSettingsElement(
    clearAndNavigate: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    composable(route = Direction.Settings.route) {
        SettingsElement(
            restartApp = clearAndNavigate,
            routeToLogin = onLoginClick
        )
    }
}
