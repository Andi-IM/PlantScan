package com.github.andiim.plantscan.app.ui.screens.home.history

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.homeHistoryElement(
    onDetailClick: (String) -> Unit,
) {
    composable(route = Direction.History.route) {
        HistoryRoute(toDetail = onDetailClick)
    }
}
