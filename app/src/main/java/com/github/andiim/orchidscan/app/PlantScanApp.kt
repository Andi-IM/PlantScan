package com.github.andiim.orchidscan.app

import android.content.res.Resources
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.andiim.orchidscan.app.ui.common.composables.BottomBar
import com.github.andiim.orchidscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.orchidscan.app.ui.navigation.SetupRootNavGraph
import com.github.andiim.orchidscan.app.ui.states.PlantScanAppState
import com.github.andiim.orchidscan.app.ui.theme.PlantScanTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun OrchidScanApp() {
    PlantScanTheme {
        val appState = rememberAppState()

        Surface(color = MaterialTheme.colorScheme.background) {
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
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        PlantScanAppState(
            snackbarHostState, navController, snackbarManager, resources, coroutineScope
        )
    }
