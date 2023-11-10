package com.github.andiim.plantscan.feature.findplant.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.findplant.FindPlantRoute

fun NavController.navigateToFindPlant(navOptions: NavOptions? = null) {
    this.navigate(FindPlantGraph.route, navOptions)
}

object FindPlantGraph : AppDestination {
    override val route: String = "find_plant_graph"
}

object FindPlant : AppDestination {
    override val route: String = "find_plant_route"
}

fun NavGraphBuilder.findPlantGraph(
    onItemClick: (String) -> Unit,
    onCameraClick: () -> Unit,
    onPlantsClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = FindPlantGraph.route,
        startDestination = FindPlant.route,
    ) {
        composable(FindPlant.route) {
            FindPlantRoute(
                modifier = Modifier.fillMaxSize(),
                onPlantClick = onItemClick,
                onCameraClick = onCameraClick,
                onPlantsClick = onPlantsClick,
            )
        }
        nestedGraphs()
    }
}
