package com.github.andiim.plantscan.feature.category.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.feature.category.PlantCategoryScreen

const val categoryRoute = "category"

fun NavGraphBuilder.categoryScreen(
    onBackClick: () -> Unit,
    toPlantScreen: (String) -> Unit,
) {
  composable(
      route = categoryRoute,
  ) {
    PlantCategoryScreen(onBackClick = onBackClick, toPlantListScreen = toPlantScreen)
  }
}
