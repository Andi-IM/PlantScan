package com.github.andiim.plantscan.feature.detect.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.detect.DetectRoute
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding: String = Charsets.UTF_8.name()

internal class DetectArgs(val detectUri: String) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        URLDecoder.decode(
            checkNotNull(savedStateHandle[Detect.detectArg]),
            urlCharacterEncoding,
        ),
    )
}

fun NavController.navigateToDetection(uri: String) {
    val encodedUri = URLEncoder.encode(uri, urlCharacterEncoding)
    this.navigate("${Detect.route}/$encodedUri") {
        launchSingleTop = true
    }
}

object Detect : AppDestination {
    override val route: String = "detect_route"
    const val detectArg = "detect_uri"
    val routeWithArgs = "$route/{$detectArg}"
    val arguments = listOf(
        navArgument(detectArg) { type = NavType.StringType },
    )
}

fun NavGraphBuilder.detectScreen(
    onBackClick: () -> Unit,
    onSuggestClick: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
) {
    composable(route = Detect.routeWithArgs, arguments = Detect.arguments) {
        DetectRoute(
            onBackClick = onBackClick,
            onSuggestClick = onSuggestClick,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
