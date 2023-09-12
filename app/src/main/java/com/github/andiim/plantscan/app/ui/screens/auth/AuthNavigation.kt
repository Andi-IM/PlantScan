package com.github.andiim.plantscan.app.ui.screens.auth

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun PlantScanAppState.navigateFromAuthRoute() {
    this.navigateAndPopUp(Direction.Settings.route, Direction.Login.route)
}

fun PlantScanAppState.navigateToAuthRoute() {
    this.navigate(Direction.Login.route)
}

fun NavGraphBuilder.authScreen(
    onAuthClick: () -> Unit,
    onLinkClick: (String) -> Unit,
) {
    composable(route = Direction.Login.route) {
        AuthRoute(navigateFromLogin = onAuthClick, openWeb = onLinkClick)
    }
}
