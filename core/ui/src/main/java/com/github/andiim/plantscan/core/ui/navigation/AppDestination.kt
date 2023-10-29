package com.github.andiim.plantscan.core.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

interface AppDestination {
    val icon: ImageVector?
    val route: String
}
