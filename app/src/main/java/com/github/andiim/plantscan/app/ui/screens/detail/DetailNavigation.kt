package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.ui.navigation.NavigationConstants
import org.jetbrains.annotations.VisibleForTesting
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val plantIdArg = "plant_id"

internal class DetailArgs(val plantId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[plantIdArg]),
                    urlCharacterEncoding
                )
            )
}

fun PlantScanAppState.navigateToDetail(id: String) {
    val encodedId = URLEncoder.encode(id, urlCharacterEncoding)
    this.navigate(Direction.Detail.createRoute(encodedId))
}

fun NavGraphBuilder.detailScreen(appState: PlantScanAppState) {
    composable(
        route = Direction.Detail.route,
        arguments = listOf(navArgument(plantIdArg) { type = NavType.StringType }),
        deepLinks =
        listOf(navDeepLink {
            uriPattern = "${NavigationConstants.APP_URI}/${Direction.Detail.route}/{$plantIdArg}"
        })
    ) {
        val viewModel: DetailViewModel = hiltViewModel()
        DetailRoute(popUpScreen = appState::popUp, viewModel = viewModel)
    }
}