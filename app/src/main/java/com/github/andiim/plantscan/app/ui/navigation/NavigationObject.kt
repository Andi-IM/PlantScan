package com.github.andiim.plantscan.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocalFlorist
import com.github.andiim.plantscan.app.R.string as AppText

object NavigationObject {
    val bottomNavItems =
        listOf(
            NavigationItem(
                title = AppText.label_search,
                icon = Icons.Filled.Search,
                direction = Direction.FindPlant
            ),
            NavigationItem(
                title = AppText.label_garden_screen,
                icon = Icons.Outlined.LocalFlorist,
                direction = Direction.History
            ),
            NavigationItem(
                title = AppText.label_settings,
                icon = Icons.Filled.List,
                direction = Direction.Settings
            ),
        )
}
