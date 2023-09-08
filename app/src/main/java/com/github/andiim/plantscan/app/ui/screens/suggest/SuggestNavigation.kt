package com.github.andiim.plantscan.app.ui.screens.suggest

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction
import org.jetbrains.annotations.VisibleForTesting
import java.net.URLDecoder
import java.net.URLEncoder

private val urlCharacterEncoding = Charsets.UTF_8.name()

@VisibleForTesting
internal const val plantIdArg = "plant_id"

internal class SuggestArgs(val plantId: String) {
    constructor(savedStateHandle: SavedStateHandle) :
            this(
                URLDecoder.decode(
                    checkNotNull(savedStateHandle[plantIdArg]),
                    urlCharacterEncoding
                )
            )
}

fun PlantScanAppState.navigateToSuggest(id: String) {
    val encodedId = URLEncoder.encode(id, urlCharacterEncoding)
    this.navigate(Direction.Suggest.createRoute(encodedId))
}

fun NavGraphBuilder.suggetScreen(
    onBackClick: () -> Unit,
) {
    composable(
        route = Direction.Suggest.route,
        arguments = listOf(navArgument(plantIdArg) { type = NavType.StringType }),
    ) {
        SuggestRoute(popUpScreen = onBackClick)
    }
}