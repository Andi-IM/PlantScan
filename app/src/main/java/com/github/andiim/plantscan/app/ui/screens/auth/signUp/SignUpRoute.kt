package com.github.andiim.plantscan.app.ui.screens.auth.signUp

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.authSignUpScreen(appState: PlantScanAppState) {
    composable(route = Direction.SignUp.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.AccountNav.route)
            }
        val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
        SignUpScreen(openAndPopUp = appState::navigateAndPopUp, viewModel = viewModel)
    }
}