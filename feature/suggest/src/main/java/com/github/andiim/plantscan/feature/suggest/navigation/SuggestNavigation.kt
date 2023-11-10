package com.github.andiim.plantscan.feature.suggest.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.suggest.SuggestRoute

fun NavController.navigateToSuggest() {
    this.navigate(Suggest.route) { launchSingleTop = true }
}

object Suggest : AppDestination {
    override val route: String = "suggest"
}

fun NavGraphBuilder.suggestScreen(
    onBackClick: () -> Unit,
    onLoginPressed: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
) {
    composable(
        route = Suggest.route,
    ) {
        SuggestRoute(
            onBackPressed = onBackClick,
            onShowSnackbar = onShowSnackbar,
            onLoginPressed = onLoginPressed,
        )
    }
}
