package com.github.andiim.plantscan.feature.plant.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.plant.PlantRoute

fun NavController.navigateToPlants() {
    this.navigate(Plant.route) { launchSingleTop = true }
}

object Plant : AppDestination {
    override val route: String = "plant_route"
}

fun NavGraphBuilder.plantScreen(
    onBackClick: () -> Unit,
    onPlantClick: (String) -> Unit,
) {
    composable(Plant.route) {
        PlantRoute(onBackClick = onBackClick, onPlantClick = onPlantClick)
    }
}
