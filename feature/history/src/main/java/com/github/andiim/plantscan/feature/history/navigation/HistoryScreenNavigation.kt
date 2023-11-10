package com.github.andiim.plantscan.feature.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.history.HistoryRoute

fun NavController.navigateToHistory(navOptions: NavOptions? = null) {
    this.navigate(History.route, navOptions)
}

object History : AppDestination {
    override val route: String = "history_route"
}

fun NavGraphBuilder.historyScreen(onDetailClick: (String) -> Unit) {
    composable(route = History.route) {
        HistoryRoute(onItemClick = onDetailClick)
    }
}
