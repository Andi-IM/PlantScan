package com.github.andiim.plantscan.feature.detect.detail.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.detect.detail.DetectDetailRoute
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding: String = Charsets.UTF_8.name()

internal class HistoryArgs(val id: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(savedStateHandle[DetectDetail.historyArg]),
            urlCharacterEncoding,
        ),
    )
}

fun NavController.navigateToDetectionDetail(uri: String) {
    val encodedUri = URLEncoder.encode(uri, urlCharacterEncoding)
    this.navigate("${DetectDetail.route}/$encodedUri") {
        launchSingleTop = true
    }
}

object DetectDetail : AppDestination {
    override val route: String = "detect_route"
    const val historyArg = "uri"
    val routeWithArgs = "$route/{$historyArg}"
    val arguments = listOf(
        navArgument(historyArg) { type = NavType.StringType },
    )
}

fun NavGraphBuilder.detectDetailScreen(
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
) {
    composable(route = DetectDetail.routeWithArgs, arguments = DetectDetail.arguments) {
        DetectDetailRoute(onBackClick = onBackClick, onSuggestClick = onSuggestClick)
    }
}

