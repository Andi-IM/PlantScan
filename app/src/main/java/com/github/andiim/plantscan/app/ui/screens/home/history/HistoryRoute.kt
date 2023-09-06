package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.homeHistoryElement(appState: PlantScanAppState) {
    composable(route = Direction.MyGarden.route) {
        val viewModel: MyGardenViewModel = hiltViewModel()
        MyGardenElement(
            toDetail = { appState.navigate(Direction.Detect.route) },
            viewModel = viewModel
        )
    }
}