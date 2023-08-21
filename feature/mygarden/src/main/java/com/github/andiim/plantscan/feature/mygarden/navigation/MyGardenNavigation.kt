package com.github.andiim.plantscan.feature.mygarden.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.feature.mygarden.MyGardenElement

const val myGardenRoute = "my_garden"

fun NavController.navigateToMyGarden(navOptions: NavOptions? = null) {
  this.navigate(com.github.andiim.plantscan.feature.mygarden.navigation.myGardenRoute, navOptions)
}

fun NavGraphBuilder.myGardenScreen(onDetailClick: (String) -> Unit) {
  composable(route = com.github.andiim.plantscan.feature.mygarden.navigation.myGardenRoute) {
    MyGardenElement(toDetail = onDetailClick)
  }
}
