package com.github.andiim.plantscan.app.ui.screens.web

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.app.ui.navigation.Direction
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding = Charsets.UTF_8.name()

internal const val URL_ARG = "url"

internal class WebArgs(val url: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(
                savedStateHandle[URL_ARG],
            ),
            urlCharacterEncoding
        )
    )
}

fun NavController.navigateToWeb(url: String) {
    val encodedUrl = URLEncoder.encode(url, urlCharacterEncoding)
    this.navigate(Direction.Web.setUrl(encodedUrl)) { launchSingleTop = false }
}

fun NavGraphBuilder.webViewScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = Direction.Web.route,
        arguments = listOf(navArgument(URL_ARG) { type = NavType.StringType })
    ) {
        WebRoute(popUpScreen = onBackClick)
    }
}
