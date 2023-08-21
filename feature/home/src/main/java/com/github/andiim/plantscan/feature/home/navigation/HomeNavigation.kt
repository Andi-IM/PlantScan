package com.github.andiim.plantscan.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.github.andiim.plantscan.feature.home.FindPlantElement

const val findPlantRoute = "find"
const val HOME_ROUTE_PATTERN = "home"
private const val DEEP_LINK_URI_PATTERN = "plantscan://app/$findPlantRoute"

fun NavController.navigateToFindPlant(navOptions: NavOptions? = null) {
  this.navigate(findPlantRoute, navOptions)
}

fun NavGraphBuilder.findPlantScreen(
    onManualFindClick: () -> Unit,
    onCameraButtonClick: () -> Unit,
    onPlantDetailClick: (String) -> Unit,
) {
  composable(
      route = findPlantRoute,
      deepLinks = listOf(navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN })) {
        FindPlantElement(
            onDetails = onPlantDetailClick,
            toDetect = onCameraButtonClick,
            toPlantType = onManualFindClick)
      }
}
