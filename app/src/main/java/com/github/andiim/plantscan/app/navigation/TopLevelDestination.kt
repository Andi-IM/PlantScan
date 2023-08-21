package com.github.andiim.plantscan.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.ui.graphics.vector.ImageVector
import com.github.andiim.plantscan.app.R

/**
 * Type for the top level destinations in the application. Each of these destinations can contain
 * one or more screens (based on the window size). Navigation from one screen to the next within a
 * single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
    val titleTextId: Int,
) {
  SEARCH(
      selectedIcon = Icons.Filled.Search,
      unselectedIcon = Icons.Filled.Search,
      iconTextId = R.string.label_search,
      titleTextId = R.string.label_search,
  ),
  MY_GARDEN(
      selectedIcon = Icons.Outlined.LocalFlorist,
      unselectedIcon = Icons.Outlined.LocalFlorist,
      iconTextId = R.string.label_garden_screen,
      titleTextId = R.string.label_garden_screen,
  ),
  SETTINGS(
      selectedIcon = Icons.Filled.List,
      unselectedIcon = Icons.Filled.List,
      iconTextId = R.string.label_settings,
      titleTextId = R.string.label_settings,
  )
}
