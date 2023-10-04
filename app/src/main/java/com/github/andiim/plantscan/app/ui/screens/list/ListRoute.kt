package com.github.andiim.plantscan.app.ui.screens.list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavController.navigateToList() {
    this.navigate(Direction.List.route){launchSingleTop = true}
}

fun NavGraphBuilder.listScreen(onBackClick: () -> Unit, onDetailClick: (String) -> Unit) {
    composable(route = Direction.List.route) {
        PlantListScreen(toDetails = onDetailClick, popUpScreen = onBackClick)
    }
}
