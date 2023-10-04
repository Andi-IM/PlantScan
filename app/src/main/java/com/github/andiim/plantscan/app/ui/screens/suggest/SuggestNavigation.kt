package com.github.andiim.plantscan.app.ui.screens.suggest

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.app.ui.navigation.Direction
import org.jetbrains.annotations.VisibleForTesting
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val PLANT_ID_ARG = "plant_id"

internal class SuggestArgs(val plantId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[PLANT_ID_ARG]),
                    urlCharacterEncoding
                )
            )
}

fun NavController.navigateToSuggest(id: String) {
    val encodedId = URLEncoder.encode(id, urlCharacterEncoding)
    this.navigate(Direction.Suggest.createRoute(encodedId)) { launchSingleTop = true }
}

fun NavGraphBuilder.suggestScreen(
    onBackClick: () -> Unit,
    onAuthClick: () -> Unit,
) {
    composable(
        route = Direction.Suggest.route,
        arguments = listOf(navArgument(PLANT_ID_ARG) { type = NavType.StringType }),
    ) {
        SuggestRoute(popUpScreen = onBackClick, onAuthClick = onAuthClick)
    }
}
