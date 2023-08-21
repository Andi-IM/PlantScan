package com.github.andiim.plantscan.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.feature.login.LoginScreen

const val loginRoute = "login"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
  this.navigate(com.github.andiim.plantscan.feature.login.navigation.loginRoute, navOptions)
}

fun NavGraphBuilder.loginGraphs(
    onWebClicked: (String) -> Unit,
    navigateAndPopUp: (String, String) -> Unit,
) {
  composable(route = com.github.andiim.plantscan.feature.login.navigation.loginRoute) {
    LoginScreen(
        openSettingsAndPopUpLogin = navigateAndPopUp,
        openWeb = onWebClicked, // single launch
    )
  }
}
