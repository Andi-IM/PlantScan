package com.github.andiim.plantscan.feature.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.andiim.plantscan.feature.settings.SettingsElement

private const val SETTINGS_PATTERN = "settings_pattern"
const val settingsRoute = "settings"

fun NavController.navigateToSettings(navOptions: NavOptions? = null) {
  this.navigate(com.github.andiim.plantscan.feature.settings.navigation.SETTINGS_PATTERN, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    clearAndNavigate: (String) -> Unit,
    openScreen: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
  navigation(startDestination = com.github.andiim.plantscan.feature.settings.navigation.settingsRoute, route = com.github.andiim.plantscan.feature.settings.navigation.SETTINGS_PATTERN) {
    composable(route = com.github.andiim.plantscan.feature.settings.navigation.settingsRoute) {
      SettingsElement(restartApp = clearAndNavigate, openScreen = openScreen)
    }
    nestedGraphs()
  }
}
