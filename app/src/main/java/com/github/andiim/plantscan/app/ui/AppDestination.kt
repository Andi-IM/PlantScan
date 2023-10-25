package com.github.andiim.plantscan.app.ui

import androidx.compose.ui.graphics.vector.ImageVector

interface AppDestination {
    val icon: ImageVector?
    val route: String
}

object Camera: AppDestination {
    override val icon: ImageVector? = null
    override val route: String = "camera"
}