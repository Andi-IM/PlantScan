package com.github.andiim.plantscan.app.ui.screens.home.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.navigation.Direction

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
