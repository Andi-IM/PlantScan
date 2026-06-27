package com.github.andiim.plantscan.app.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.github.andiim.plantscan.app.navigation.TopLevelDestination
import com.github.andiim.plantscan.app.navigation.TopLevelDestination.FIND_PLANT
import com.github.andiim.plantscan.app.navigation.TopLevelDestination.HISTORY
import com.github.andiim.plantscan.app.navigation.TopLevelDestination.SETTINGS
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.core.ui.TrackDisposableJank
import com.github.andiim.plantscan.feature.findplant.navigation.FindPlant
import com.github.andiim.plantscan.feature.findplant.navigation.navigateToFindPlant
import com.github.andiim.plantscan.feature.history.navigation.History
import com.github.andiim.plantscan.feature.history.navigation.navigateToHistory
import com.github.andiim.plantscan.feature.settings.navigation.Settings
import com.github.andiim.plantscan.feature.settings.navigation.navigateToSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberPsAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): PsAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
    ) {
        PsAppState(
            navController,
            coroutineScope,
            windowSizeClass,
            networkMonitor,
        )
    }
}

@Stable
class PsAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            FindPlant.route -> FIND_PLANT
            History.route -> HISTORY
            Settings.route -> SETTINGS
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                FIND_PLANT -> navController.navigateToFindPlant(topLevelNavOptions)
                HISTORY -> navController.navigateToHistory(topLevelNavOptions)
                SETTINGS -> navController.navigateToSettings(topLevelNavOptions)
            }
        }
    }
}

/** Stores information about navigation events to be used with JankStats. */
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
