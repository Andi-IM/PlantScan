package com.github.andiim.plantscan.app

import android.content.Context
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.andiim.plantscan.app.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.app.core.ui.TrackDisposableJank
import com.github.andiim.plantscan.app.ui.common.composables.BottomBar
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.navigation.SetupRootNavGraph
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlantScanApp(
    networkMonitor: NetworkMonitor,
    appState: PlantScanAppState = rememberAppState(
        networkMonitor = networkMonitor,
    )
) {
    PlantScanTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

            val isOffline by appState.isOffline.collectAsStateWithLifecycle()

            LaunchedEffect(isOffline) {
                if (isOffline) {
                    SnackbarManager.showMessage(
                        R.string.not_connected,
                        duration = SnackbarDuration.Indefinite
                    )
                }
            }

            Scaffold(
                modifier = Modifier.semantics {
                    testTagsAsResourceId = true
                },
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackbarData ->
                            Snackbar(
                                snackbarData,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        })
                },
                bottomBar = {
                    BottomBar(
                        navigate = appState::clearAndNavigate,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag("BottomBar")
                    )
                }
            ) { innerPadding ->
                SetupRootNavGraph(appState, modifier = Modifier.padding(innerPadding))
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

@Composable
fun rememberAppState(
    networkMonitor: NetworkMonitor,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    getContext: Context = getContext(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): PlantScanAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        snackbarHostState,
        snackbarManager,
        getContext,
        networkMonitor,
        coroutineScope,
    ) {
        PlantScanAppState(
            navController,
            snackbarHostState,
            snackbarManager,
            getContext,
            networkMonitor,
            coroutineScope,
        )
    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}