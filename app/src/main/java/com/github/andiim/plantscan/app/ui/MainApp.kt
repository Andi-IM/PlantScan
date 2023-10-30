package com.github.andiim.plantscan.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.app.navigation.PsHost
import com.github.andiim.plantscan.app.navigation.TopLevelDestination
import com.github.andiim.plantscan.app.navigation.TopLevelDestination.FIND_PLANT
import com.github.andiim.plantscan.app.utils.BOTTOM_BAR_TAG
import com.github.andiim.plantscan.app.utils.NAV_RAIL_TAG
import com.github.andiim.plantscan.app.utils.snackbar.showMessage
import com.github.andiim.plantscan.core.data.util.NetworkMonitor
import com.github.andiim.plantscan.core.designsystem.component.PlantScanBackground
import com.github.andiim.plantscan.core.designsystem.component.PsAnimatedVisibility
import com.github.andiim.plantscan.core.designsystem.component.PsAnimatedVisibilityData
import com.github.andiim.plantscan.core.designsystem.component.PsNavigationBar
import com.github.andiim.plantscan.core.designsystem.component.PsNavigationBarItem
import com.github.andiim.plantscan.core.designsystem.component.PsNavigationRail
import com.github.andiim.plantscan.core.designsystem.component.PsNavigationRailItem
import com.github.andiim.plantscan.core.designsystem.component.PsTopAppBar
import timber.log.Timber

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
)
@Suppress("detekt:LongMethod")
@Composable
fun MainApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    modifier: Modifier = Modifier,
    appState: PsAppState = rememberPsAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass,
    ),
) {
    PlantScanBackground {
        val snackbarHostState = remember { SnackbarHostState() }
        val isOffline by appState.isOffline.collectAsStateWithLifecycle()
        val message = stringResource(R.string.not_connected)
        val destination = appState.currentTopLevelDestination
        LaunchedEffect(isOffline) {
            if (isOffline) {
                snackbarHostState.showMessage(
                    message = message,
                    duration = SnackbarDuration.Indefinite,
                )
            }
        }

        Scaffold(
            modifier = modifier
                .semantics {
                    testTagsAsResourceId = true
                },
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                PsAnimatedVisibility(
                    destination != null && appState.shouldShowBottomBar,
                    animatedData = PsAnimatedVisibilityData.BottomBar,
                ) {
                    PsBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag(BOTTOM_BAR_TAG),
                    )
                }
            },
        ) { paddingValues ->
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues)
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Horizontal,
                        ),
                    ),
            ) {
                PsAnimatedVisibility(destination != null && appState.shouldShowNavRail) {
                    PsNavRail(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier
                            .testTag(NAV_RAIL_TAG)
                            .safeDrawingPadding(),
                    )
                }
                Column(modifier.fillMaxSize()) {
                    if (destination != null && destination != FIND_PLANT) {
                        PsTopAppBar(titleRes = destination.titleTextId)
                    }
                    PsHost(
                        appState = appState,
                        onShowSnackbar = { message, action, duration ->
                            snackbarHostState.showMessage(
                                message = message,
                                actionLabel = action,
                                duration = duration,
                            ) == SnackbarResult.ActionPerformed
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun PsNavRail(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    PsNavigationRail(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            Timber.d("${destination.name} SELECTED $selected")
            PsNavigationRailItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = Modifier,
            )
        }
    }
}

@Composable
fun PsBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    PsNavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            PsNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = Modifier,
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false
