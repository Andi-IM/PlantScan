package com.github.andiim.plantscan.app.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.github.andiim.plantscan.app.R
import com.github.andiim.plantscan.feature.home.navigation.HOME_ROUTE_PATTERN
import com.github.andiim.plantscan.app.navigation.SetupRootNavGraph
import com.github.andiim.plantscan.app.navigation.TopLevelDestination
import com.github.andiim.plantscan.core.ui.PermissionDialog
import com.github.andiim.plantscan.core.ui.RationaleDialog
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme
import com.github.andiim.plantscan.data.util.NetworkMonitor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun PlantScanApp(
    networkMonitor: NetworkMonitor,
    appState: PlantScanAppState = rememberAppState(networkMonitor = networkMonitor),
) {
  PlantScanTheme {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      RequestNotificationPermissionDialog()
    }

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()
    // Show snackbar when internet not show
    val notConnectedMessage = stringResource(R.string.not_connected)
    LaunchedEffect(isOffline) {
      if (isOffline) {
        appState.snackbarHostState.showSnackbar(
            message = notConnectedMessage,
            duration = SnackbarDuration.Indefinite,
        )
      }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
      Scaffold(
          snackbarHost = {
            SnackbarHost(
                hostState = appState.snackbarHostState,
                modifier = Modifier.padding(8.dp),
                snackbar = { snackbarData ->
                  Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                })
          },
          bottomBar = {
            BottomBar(
                destinations = appState.topLevelDestinations,
                onNavigateToDestination = appState::navigateToTopLevelDestination,
                currentDestination = appState.currentDestination,
            )
          },
      ) { innerPadding ->
        SetupRootNavGraph(appState, modifier = Modifier.padding(innerPadding))
      }
    }
  }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog() {
  val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

  if (!permissionState.status.isGranted) {
    if (permissionState.status.shouldShowRationale) RationaleDialog()
    else PermissionDialog { permissionState.launchPermissionRequest() }
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = currentDestination?.hierarchy?.any { it.route == HOME_ROUTE_PATTERN } == true,
        modifier = modifier,
    ) {
        NavigationBar(
            modifier =
            Modifier.animateEnterExit(
                enter =
                slideInVertically {
                    // Slide in from 40 dp from the top.
                    with(density) { -40.dp.roundToPx() }
                } +
                        expandVertically(
                            // Expand from the top.
                            expandFrom = Alignment.Top) +
                        fadeIn(
                            // Fade in with the initial alpha of 0.3f.
                            initialAlpha = 0.3f),
                exit = slideOutVertically() + shrinkVertically() + fadeOut())) {
            destinations.forEach { destination ->
                val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
                PlantScanNavigationBarItem(
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
                    modifier = Modifier)
            }
        }
    }
}

@Composable
private fun RowScope.PlantScanNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
        colors =
        NavigationBarItemDefaults.colors(
            selectedIconColor = PlantScanNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = PlantScanNavigationDefaults.navigationContentColor(),
            selectedTextColor = PlantScanNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = PlantScanNavigationDefaults.navigationContentColor(),
            indicatorColor = PlantScanNavigationDefaults.navigationIndicatorColor(),
        ),
    )
}

/** Plant scan default values */
object PlantScanNavigationDefaults {
    @Composable fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } ?: false
