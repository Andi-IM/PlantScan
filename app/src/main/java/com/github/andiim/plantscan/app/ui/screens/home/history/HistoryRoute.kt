package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.homeHistoryElement() {
    composable(route = Direction.MyGarden.route) {
        HistoryRoute(toDetail = { /* TODO: SOMETIMES IMPLEMENT TO DETECTION DETAIL */ })
    }
}