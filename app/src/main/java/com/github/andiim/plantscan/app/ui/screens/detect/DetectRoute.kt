package com.github.andiim.plantscan.app.ui.screens.detect

import androidx.core.net.toUri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction

fun NavGraphBuilder.detectFragment(appState: PlantScanAppState) {
    composable(
        route = Direction.Detect.route,
        arguments = listOf(navArgument("imageUri") { type = NavType.StringType })
    ) { backStackEntry ->
        val imageUri = backStackEntry.arguments?.getString("imageUri")
        imageUri?.let { DetectScreen(imageUri = it.toUri()) }
    }
}
