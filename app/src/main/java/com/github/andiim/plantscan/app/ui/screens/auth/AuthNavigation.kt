package com.github.andiim.plantscan.app.ui.screens.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavController.navigateFromAuthRoute() {
    this.navigate(Direction.Settings.route) {
        launchSingleTop = true
        popUpTo(Direction.Login.route) { inclusive = true }
    }
}

fun NavController.navigateToAuthRoute() {
    this.navigate(Direction.Login.route) { launchSingleTop = true }
}

fun NavGraphBuilder.authScreen(
    onAuthClick: () -> Unit,
    onLinkClick: (String) -> Unit,
) {
    composable(route = Direction.Login.route) {
        AuthRoute(navigateFromLogin = onAuthClick, openWeb = onLinkClick)
    }
}
