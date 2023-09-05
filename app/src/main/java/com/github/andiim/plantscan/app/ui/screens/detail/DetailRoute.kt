package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.ui.navigation.NavigationConstants


fun PlantScanAppState.navigateToDetail(id: String){
    this.navigate(Direction.Detail.createRoute(id))
}

fun NavGraphBuilder.detailScreen(appState: PlantScanAppState) {
    composable(
        route = Direction.Detail.route,
        arguments = listOf(navArgument("orchid_id") { type = NavType.StringType }),
        deepLinks =
        listOf(navDeepLink { uriPattern = "${NavigationConstants.APP_URI}/${Direction.Detail.route}/{orchid_id}" })
    ) {
            backStackEntry ->
        val viewModel: DetailViewModel = hiltViewModel()
        val id = backStackEntry.arguments?.getString("orchid_id")
        DetailScreen(id = id, popUpScreen = appState::popUp, viewModel = viewModel)
    }
}