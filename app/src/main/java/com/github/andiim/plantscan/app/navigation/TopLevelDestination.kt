package com.github.andiim.plantscan.app.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.feature.findplant.R as findPlantR
import com.github.andiim.plantscan.feature.history.R as historyR
import com.github.andiim.plantscan.feature.settings.R as settingsR

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
    FIND_PLANT(
        selectedIcon = PsIcons.Home,
        unselectedIcon = PsIcons.HomeBorder,
        iconTextId = findPlantR.string.find_plant,
        titleTextId = findPlantR.string.find_plant,
    ),
    HISTORY(
        selectedIcon = PsIcons.Garden,
        unselectedIcon = PsIcons.GardenBorder,
        iconTextId = historyR.string.detection_history,
        titleTextId = historyR.string.detection_history,
    ),
    SETTINGS(
        selectedIcon = PsIcons.Settings,
        unselectedIcon = PsIcons.SettingsBorder,
        iconTextId = settingsR.string.settings,
        titleTextId = settingsR.string.settings,
    ),
}
