package com.github.andiim.orchidscan.app.ui.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.andiim.orchidscan.app.rememberAppState
import com.github.andiim.orchidscan.app.ui.navigation.Direction
import com.github.andiim.orchidscan.app.ui.navigation.NavigationObject
import com.github.andiim.orchidscan.app.ui.states.PlantScanAppState

@Composable
fun HomeScreen(appState: PlantScanAppState = rememberAppState()) {
  Scaffold(
      bottomBar = {},
      floatingActionButton = {
        FloatingActionButton(onClick = { appState.navigate(Direction.Detect.route) }) {
          Icon(Icons.Filled.Camera, contentDescription = "Detect!")
        }
      }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
          /*when (selectedItem) {
            0 -> SearchOrDetectOrchidElement()
            1 -> MyGardenElement()
            2 -> SettingsElement(restartApp = {}, openScreen = {})
          }*/
        }
      }
}

@Composable
fun BottomBar(state: PlantScanAppState) {
  val navBackStateEntry by state.navController.currentBackStackEntryAsState()
  val currentDestination = navBackStateEntry?.destination
  if (currentDestination?.hierarchy?.any { it.route == Direction.MainNav.route } == true)
      NavigationBar {
        for (navigationItem in NavigationObject.bottomNavItems) {
          Items(
              state = state,
              title = navigationItem.title,
              icon = navigationItem.icon,
              direction = navigationItem.direction)
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
