package com.github.andiim.plantscan.app.ui.screens.detect

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding = Charsets.UTF_8.name()

internal const val detectUriArg = "imageUri"

internal class DetectArgs(val uri: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(savedStateHandle[detectUriArg]),
            urlCharacterEncoding
        )
    )
}

fun PlantScanAppState.navigateToDetect(uri: String) {
    val encodedId = URLEncoder.encode(uri, urlCharacterEncoding)
    this.navigate(Direction.Detect.createRoute(encodedId), false)
}

fun NavGraphBuilder.detectFragment(
    backToTop: () -> Unit,
    onSuggestClick: (String) -> Unit,
) {
    composable(
        route = Direction.Detect.route,
        arguments = listOf(navArgument(detectUriArg) { type = NavType.StringType })
    ) {
        DetectRoute(
            backToTop = backToTop,
            onSuggestClick = onSuggestClick
        )
    }
}
