package com.github.andiim.plantscan.app.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.github.andiim.plantscan.data.util.NetworkMonitor
import com.github.andiim.plantscan.feature.home.navigation.navigateToFindPlant
import com.github.andiim.plantscan.feature.mygarden.navigation.navigateToMyGarden
import com.github.andiim.plantscan.feature.settings.navigation.navigateToSettings
import com.github.andiim.plantscan.app.navigation.TopLevelDestination
import com.github.andiim.plantscan.core.ui.snackbar.SnackbarManager
import com.github.andiim.plantscan.core.ui.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    networkMonitor: NetworkMonitor,
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    getContext: Context = getContext(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): PlantScanAppState {
  return remember(
      snackbarHostState,
      navController,
      snackbarManager,
      getContext,
      networkMonitor,
      coroutineScope,
  ) {
    PlantScanAppState(
        snackbarHostState,
        navController,
        snackbarManager,
        getContext,
        networkMonitor,
        coroutineScope,
    )
  }
}

@Stable
class PlantScanAppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val context: Context,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope
) {
  init {
    coroutineScope.launch {
      snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
        val text = snackbarMessage.toMessage(context.resources)
        snackbarHostState.showSnackbar(text)
      }
    }
  }

  val currentDestination: NavDestination?
    @Composable get() = navController.currentBackStackEntryAsState().value?.destination

  val isOffline =
      networkMonitor.isOnline
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
   * UI logic for navigating to a top level destination in the app. Top level destinations have only
   * one copy of the destination of the back stack, and save and restore state whenever you navigate
   * to and from it.
   *
   * @param topLevelDestination: The destination the app needs to navigate to.
   */
  fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
    trace("Navigation: ${topLevelDestination.name}") {
      val topLevelNavOptions = navOptions {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
      }

      when (topLevelDestination) {
        TopLevelDestination.SEARCH -> navController.navigateToFindPlant(topLevelNavOptions)
        TopLevelDestination.MY_GARDEN -> navController.navigateToMyGarden(topLevelNavOptions)
        TopLevelDestination.SETTINGS -> navController.navigateToSettings(topLevelNavOptions)
      }
    }
  }
}

@Composable
@ReadOnlyComposable
fun getContext(): Context {
  LocalConfiguration.current
  return LocalContext.current
}
