package com.github.andiim.plantscan.app.ui.screens.home.findPlant

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.ui.navigation.NavigationConstants

fun PlantScanAppState.gotoMainNavRoute() {
    this.navigateAndPopUp(Direction.MainNav.route, Direction.Splash.route)
}

fun NavGraphBuilder.homeFindPlantElement(
    routeToDetail: (String) -> Unit,
    routeToCamera: () -> Unit,
    routeToList: () -> Unit = {},
) {
    composable(
        route = Direction.FindPlant.route,
        deepLinks = listOf(navDeepLink {
            uriPattern = "${NavigationConstants.APP_URI}/${Direction.FindPlant.route}"
        })
    ) {
        FindPlantRoute(
            onDetails = routeToDetail,
            toDetect = routeToCamera,
            toPlantType = routeToList,
        )
    }
}