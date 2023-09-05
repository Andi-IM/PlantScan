package com.github.andiim.plantscan.app.ui.screens.auth.login

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.authLoginScreen(appState: PlantScanAppState) {
    composable(route = Direction.Login.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.AccountNav.route)
            }
        val viewModel: LoginViewModel = hiltViewModel(parentEntry)
        LoginScreen(
            openAndPopUp = appState::navigateAndPopUp,
            openWeb = { url -> appState.navigate(Direction.Web.setUrl(url), singleTopLaunch = false) },
            viewModel = viewModel)
    }
}