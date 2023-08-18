package com.github.andiim.plantscan.app.ui.common.composables

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.andiim.plantscan.app.PlantScanAppState
import com.github.andiim.plantscan.app.ui.navigation.Direction
import com.github.andiim.plantscan.app.ui.navigation.NavigationObject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomBar(state: PlantScanAppState) {
  val density = LocalDensity.current
  val navBackStateEntry by state.navController.currentBackStackEntryAsState()
  val currentDestination = navBackStateEntry?.destination
  AnimatedVisibility(
      visible =
          currentDestination?.hierarchy?.any { it.route == Direction.MainNav.route } == true) {
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
              for (navigationItem in NavigationObject.bottomNavItems) {
                Items(
                    state = state,
                    title = navigationItem.title,
                    icon = navigationItem.icon,
                    direction = navigationItem.direction)
              }
            }
      }
}

@Composable
private fun RowScope.Items(
    state: PlantScanAppState,
    @StringRes title: Int,
    icon: ImageVector,
    direction: Direction
) {
  val navBackStackEntry by state.navController.currentBackStackEntryAsState()
  val currentDestination = navBackStackEntry?.destination

  NavigationBarItem(
      icon = { Icon(imageVector = icon, contentDescription = stringResource(title)) },
      selected = currentDestination?.hierarchy?.any { it.route == direction.route } == true,
      label = { Text(stringResource(title)) },
      onClick = { state.clearAndNavigate(direction.route) },
      alwaysShowLabel = false)
}
