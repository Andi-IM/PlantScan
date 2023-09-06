package com.github.andiim.plantscan.app.ui.screens.splash

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.splashScreen(navigateAndPopUp: () -> Unit) {
    composable(route = Direction.Splash.route) {
        SplashScreen(openScreen = navigateAndPopUp)
    }
}