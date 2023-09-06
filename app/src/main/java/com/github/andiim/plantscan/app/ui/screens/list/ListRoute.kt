package com.github.andiim.plantscan.app.ui.screens.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun PlantScanAppState.navigateToList() {
    this.navigate(Direction.List.route)
}

fun NavGraphBuilder.listScreen(onBackPressed: () -> Unit, routeToDetail: (String) -> Unit) {
    composable(route = Direction.List.route) {
        PlantListScreen(toDetails = routeToDetail, popUpScreen = onBackPressed)
    }
}