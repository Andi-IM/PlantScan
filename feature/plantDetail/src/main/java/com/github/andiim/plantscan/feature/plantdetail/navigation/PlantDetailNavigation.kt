package com.github.andiim.plantscan.feature.plantdetail.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.plantdetail.PlantDetailRoute
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding: String = Charsets.UTF_8.name()

internal class PlantArgs(val plantId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            URLDecoder.decode(
                checkNotNull(savedStateHandle[PlantDetail.plantArg]),
                urlCharacterEncoding,
            ),
        )
}

object PlantDetail : AppDestination {
    override val route: String = "plant"
    const val plantArg = "plantId"
    val routeWithArgs = "$route/{$plantArg}"
    val arguments = listOf(
        navArgument(plantArg) { type = NavType.StringType },
    )
}

fun NavController.navigateToPlantDetail(plantId: String) {
    val encodedId = URLEncoder.encode(plantId, urlCharacterEncoding)
    this.navigate("${PlantDetail.route}/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.plantDetailScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = PlantDetail.routeWithArgs,
        arguments = PlantDetail.arguments,
    ) {
        PlantDetailRoute(onBackClick = onBackClick)
    }
}
