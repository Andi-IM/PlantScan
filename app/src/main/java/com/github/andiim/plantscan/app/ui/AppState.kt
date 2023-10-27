package com.github.andiim.plantscan.app.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.andiim.plantscan.app.core.utils.snackbar.SnackbarManager
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.core.ui.TrackDisposableJank
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState {
    NavigationTrackingSideEffect(navController)
    val snackbarManager = SnackbarManager
    return remember(
        navController,
        snackbarHostState,
        snackbarManager,
        networkMonitor,
        coroutineScope,
        windowSizeClass
    ) {
        AppState(
            navController,
            snackbarHostState,
            snackbarManager,
            networkMonitor,
            coroutineScope,
            windowSizeClass
        )
    }
}

class AppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { data ->
                val state = snackbarHostState.showSnackbar(data)
                if (state == SnackbarResult.ActionPerformed) {
                    data.action?.invoke()
                }
            }
        }
    }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    // TOP LEVEL DESTINATIONS

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar
}

/** Stores information about navigation events to be used with JankStats */
@Composable
fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                metricsHolder.state?.putState("Navigation", destination.route.toString())
            }

        navController.addOnDestinationChangedListener(listener)

        onDispose { navController.removeOnDestinationChangedListener(listener) }
    }
}