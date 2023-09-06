package com.github.andiim.plantscan.app

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.andiim.plantscan.app.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.app.core.ui.TrackDisposableJank
import com.github.andiim.plantscan.app.ui.common.composables.BottomBar
import com.github.andiim.plantscan.app.ui.common.composables.PermissionDialog
import com.github.andiim.plantscan.app.ui.common.composables.RationaleDialog
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.navigation.SetupRootNavGraph
import com.github.andiim.plantscan.app.ui.theme.PlantScanTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope

@Composable
fun PlantScanApp(
    networkMonitor: NetworkMonitor,
    appState: PlantScanAppState = rememberAppState(
        networkMonitor = networkMonitor,
    )
) {
    PlantScanTheme {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            RequestNotificationPermissionDialog()
        }
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
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
                bottomBar = { BottomBar(state = appState) }
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
    val permissionState =
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (!permissionState.status.isGranted) {
        if (permissionState.status.shouldShowRationale) RationaleDialog()
        else PermissionDialog { permissionState.launchPermissionRequest() }
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