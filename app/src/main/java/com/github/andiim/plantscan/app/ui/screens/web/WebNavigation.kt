package com.github.andiim.plantscan.app.ui.screens.web

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

internal const val urlArg = "url"

internal class WebArgs(val url: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(
                savedStateHandle[urlArg],
            ),
            urlCharacterEncoding
        )
    )
}

fun PlantScanAppState.navigateToWeb(url: String) {
    val encodedUrl = URLEncoder.encode(url, urlCharacterEncoding)
    this.navigate(Direction.Web.setUrl(encodedUrl), false)
}

fun NavGraphBuilder.webViewScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = Direction.Web.route,
        arguments = listOf(navArgument(urlArg) { type = NavType.StringType })
    ) {
        WebRoute(popUpScreen = onBackClick)
    }
}
