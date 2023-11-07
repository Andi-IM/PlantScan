package com.github.andiim.plantscan.feature.suggest.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.suggest.SuggestRoute
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding = Charsets.UTF_8.name()

internal class SuggestArgs(val plantId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
        this(
            URLDecoder.decode(
                checkNotNull(savedStateHandle[Suggest.suggestArg]),
                urlCharacterEncoding,
            ),
        )
}

fun NavController.navigateToSuggest(id: String) {
    val encodedId = URLEncoder.encode(id, urlCharacterEncoding)
    this.navigate("${Suggest.route}/$encodedId") { launchSingleTop = true }
}

object Suggest : AppDestination {
    override val route: String = "detect"
    const val suggestArg = "uri"
    val routeWithArgs = "$route/{$suggestArg}"
    val arguments = listOf(
        navArgument(suggestArg) { type = NavType.StringType },
    )
}

fun NavGraphBuilder.suggestScreen(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
) {
    composable(
        route = Suggest.routeWithArgs,
        arguments = Suggest.arguments,
    ) {
        SuggestRoute(onBackPressed = onBackClick, onShowSnackbar = onShowSnackbar)
    }
}
