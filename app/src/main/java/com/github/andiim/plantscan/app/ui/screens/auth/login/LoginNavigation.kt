package com.github.andiim.plantscan.app.ui.screens.auth.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction


fun PlantScanAppState.navigateToLogin(){
    this.navigate(Direction.Login.route)
}

fun NavGraphBuilder.authLoginScreen(
    navigateAndPopUp: (String, String) -> Unit,
    routeToWeb: (String) -> Unit,
) {
    composable(route = Direction.Login.route) {
        LoginRoute(openAndPopUp = navigateAndPopUp, openWeb = routeToWeb)
    }
}