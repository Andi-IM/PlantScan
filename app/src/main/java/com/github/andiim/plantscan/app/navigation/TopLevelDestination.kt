package com.github.andiim.plantscan.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.github.andiim.plantscan.core.designsystem.icon.AppIcons

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    HOME(
        selectedIcon = AppIcons.Settings,
        unselectedIcon = AppIcons.SettingsBorder,
        iconTextId = 1,
        titleTextId = 1
    ),
    SETTINGS(
        selectedIcon = AppIcons.Settings,
        unselectedIcon = AppIcons.SettingsBorder,
        iconTextId = 1,
        titleTextId = 1
    ),
    HISTORY(
        selectedIcon = AppIcons.Settings,
        unselectedIcon = AppIcons.SettingsBorder,
        iconTextId = 1,
        titleTextId = 1
    )
}