package com.github.andiim.plantscan.app.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestination {
    val icon: ImageVector?
    val route: String
}

object Camera : AppDestination {
    override val icon: ImageVector? = null
    override val route: String = "camera"
}

object Web : AppDestination {
    override val icon: ImageVector? = null
    override val route: String = "web"
    const val webArg = "url"
    val routeWithArgs = "$route/$webArg"
    val arguments = listOf(
        navArgument(webArg) { type = NavType.StringType }
    )
}