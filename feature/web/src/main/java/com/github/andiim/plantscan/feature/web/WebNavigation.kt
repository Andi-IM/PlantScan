package com.github.andiim.plantscan.feature.web

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.Web
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding: String = Charsets.UTF_8.name()

internal class WebArgs(val url: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(savedStateHandle[Web.webArg]),
            urlCharacterEncoding
        )
    )
}

fun NavController.navigateToWeb(url: String) {
    val encodedUrl = URLEncoder.encode(url, urlCharacterEncoding)
    this.navigate(Web.route)
}

fun NavGraphBuilder.webViewScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = Web.route,
        arguments = Web.arguments
    ) {
        WebRoute(popUpScreen = onBackClick)
    }
}