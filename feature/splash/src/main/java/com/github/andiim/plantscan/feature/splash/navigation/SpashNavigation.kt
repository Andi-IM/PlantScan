package com.github.andiim.plantscan.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.feature.splash.SplashScreen

const val splashRoute = "splash"
fun NavGraphBuilder.splashScreen(navigateAndPopUp: (String, String) -> Unit) {
  composable(route = splashRoute) { SplashScreen(openAndPopUp = navigateAndPopUp) }
}
