package com.github.andiim.plantscan.feature.settings.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
    this.navigate(Settings.route, navOptions)
}

object Settings : AppDestination {
    override val route: String = "settings_route"
}

fun NavGraphBuilder.settingsScreen() {
    composable(route = Settings.route) {
        Box(modifier = Modifier.fillMaxSize())
    }
}
