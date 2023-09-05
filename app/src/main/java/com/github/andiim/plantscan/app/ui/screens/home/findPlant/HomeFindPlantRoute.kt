package com.github.andiim.plantscan.app.ui.screens.home.findPlant

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.ui.navigation.NavigationConstants

fun NavGraphBuilder.homeFindPlantElement(
    routeToDetail: (String) -> Unit,
    appState: PlantScanAppState,

    ) {
    composable(
        route = Direction.FindPlant.route,
        deepLinks = listOf(navDeepLink {
            uriPattern = "${NavigationConstants.APP_URI}/${Direction.FindPlant.route}"
        })
    ) {
        val viewModel: FindPlantViewModel = hiltViewModel()
        FindPlantElement(
            onDetails = { appState.navigate(Direction.Detail.createRoute(it)) },
            viewModel = viewModel,
            toDetect = { appState.navigate(Direction.Camera.route, singleTopLaunch = false) },
            toPlantType = {})
    }
}