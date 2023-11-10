package com.github.andiim.plantscan.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.settings.SettingsRoute

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(SettingsGraphPattern.route, navOptions)
}

fun NavController.clearAndNavigateSettings() {
    this.navigate(Settings.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

object SettingsGraphPattern : AppDestination {
    override val route: String = "settings_graph"
}

object Settings : AppDestination {
    override val route: String = "settings_route"
}

fun NavGraphBuilder.settingsGraph(
    onLoginClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = SettingsGraphPattern.route,
        startDestination = Settings.route,
    ) {
        composable(Settings.route) {
            SettingsRoute(
                routeToAuth = onLoginClick,
            )
        }
        nestedGraphs()
    }
}
