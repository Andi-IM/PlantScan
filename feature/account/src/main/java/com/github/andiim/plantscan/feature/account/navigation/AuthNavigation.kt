package com.github.andiim.plantscan.feature.account.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.andiim.plantscan.core.ui.navigation.AppDestination
import com.github.andiim.plantscan.feature.account.AuthRoute

fun NavController.navigateToAuth() {
    this.navigate(Auth.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}

object Auth : AppDestination {
    override val route: String = "auth"
}

fun NavGraphBuilder.authScreen(
    onBackPressed: () -> Unit,
    authCallback: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
) {
    composable(
        Auth.route,
    ) {
        AuthRoute(
            onBackPressed = onBackPressed,
            authCallback = authCallback,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
