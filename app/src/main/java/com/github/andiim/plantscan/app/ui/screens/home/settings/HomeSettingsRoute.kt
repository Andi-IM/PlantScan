package com.github.andiim.plantscan.app.ui.screens.home.settings

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.homeSettingsElement(appState: PlantScanAppState) {
    composable(route = Direction.Settings.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.MainNav.route)
            }
        val viewModel: SettingsViewModel = hiltViewModel(parentEntry)
        SettingsElement(
            restartApp = appState::clearAndNavigate,
            openScreen = appState::navigate,
            viewModel = viewModel
        )
    }
}
