package com.github.andiim.plantscan.app.ui.screens.list

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.listScreen(appState: PlantScanAppState) {
    composable(route = Direction.List.route) { backStackEntry ->
        val parentEntry =
            remember(backStackEntry) {
                appState.navController.getBackStackEntry(Direction.MainNav.route)
            }
        val viewModel: PlantListViewModel = hiltViewModel(parentEntry)
        PlantListScreen(
            toDetails = { appState.navigate(Direction.Detail.createRoute(it)) },
            popUpScreen = appState::popUp,
            viewModel = viewModel
        )
    }
}