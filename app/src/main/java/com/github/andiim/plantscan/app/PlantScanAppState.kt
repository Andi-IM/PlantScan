package com.github.andiim.plantscan.app

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.andiim.plantscan.app.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarMessage
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarMessage.Companion.getAction
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarMessage.Companion.getDuration
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarMessage.Companion.toMessage
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarVisualsWithError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlantScanAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val snackbarManager: SnackbarManager,
    private val context: Context,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->

                val text = snackbarMessage.toMessage(context.resources)
                val action = snackbarMessage.getAction()

                if (text != null) {
                    val state = snackbarHostState
                        .showSnackbar(
                            SnackbarVisualsWithError(
                                actionTitle = context.getString(R.string.snackbar_dismiss),
                                message = text,
                                setDuration = snackbarMessage.getDuration(),
                                isError = true,
                            )
                        )
                    if (state == SnackbarResult.ActionPerformed) {
                        action?.invoke()
                    }
                } else if (snackbarMessage is SnackbarMessage.SnackbarWithLabel) {
                    val state = snackbarHostState
                        .showSnackbar(
                            SnackbarVisualsWithError(
                                actionTitle = snackbarMessage.label,
                                message = snackbarMessage.message,
                                setDuration = snackbarMessage.getDuration(),
                                isError = snackbarMessage.isError,
                            )
                        )
                    if (state == SnackbarResult.ActionPerformed) {
                        action?.invoke()
                    }
                }
            }
        }
    }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun navigate(route: String, singleTopLaunch: Boolean = true) {
        navController.navigate(route) { launchSingleTop = singleTopLaunch }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                // saveState = true
                inclusive = true
            }
            launchSingleTop = true
            // restoreState = true
        }
    }
}
