package com.github.andiim.plantscan.feature.findplant.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.findplant.FindPlantRoute

fun NavController.navigateToFindPlant() {
    this.navigate(FindPlant.route) { launchSingleTop = false }
}

object FindPlant : AppDestination {
    override val icon: ImageVector? = null
    override val route: String = "findPlant"
}

fun NavGraphBuilder.findPlantScreen(
    onItemClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onPlantClick: () -> Unit,
) {
    composable(FindPlant.route) {
        FindPlantRoute(
            modifier = Modifier.fillMaxSize(),
            onPlantClick = onItemClick,
            onCameraClick = onCameraClick,
            onPlantsClick = onPlantClick,
        )
    }
}
