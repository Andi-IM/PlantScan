package com.github.andiim.plantscan.feature.history.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.history.HistoryRoute

fun NavController.navigateToHistory() {
    this.navigate(History.route) { launchSingleTop = false }
}

object History : AppDestination {
    override val icon: ImageVector? = null
    override val route: String = "history"
}

fun NavGraphBuilder.historyScreen() {
    composable(History.route) {
        HistoryRoute()
    }
}
