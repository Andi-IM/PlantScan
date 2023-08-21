package com.github.andiim.plantscan.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import com.github.andiim.plantscan.app.ui.PlantScanAppState
import com.github.andiim.plantscan.feature.camera.navigation.imagingProcessGraph
import com.github.andiim.plantscan.feature.camera.navigation.navigateToDetectionsGraph
import com.github.andiim.plantscan.feature.detail.navigation.detailScreen
import com.github.andiim.plantscan.feature.detail.navigation.navigateToDetail
import com.github.andiim.plantscan.feature.detect.navigation.detectScreen
import com.github.andiim.plantscan.feature.detect.navigation.navigateToDetection
import com.github.andiim.plantscan.feature.home.navigation.findPlantScreen
import com.github.andiim.plantscan.feature.login.navigation.loginGraphs
import com.github.andiim.plantscan.feature.mygarden.navigation.myGardenScreen
import com.github.andiim.plantscan.feature.list.navigation.listScreen
import com.github.andiim.plantscan.feature.list.navigation.navigateToPlantList
import com.github.andiim.plantscan.app.feature.plants.navigation.navigateToPlantGraphs
import com.github.andiim.plantscan.app.feature.plants.navigation.plantsGraph
import com.github.andiim.plantscan.feature.settings.navigation.settingsScreen
import com.github.andiim.plantscan.feature.signup.navigation.authSignUpScreen
import com.github.andiim.plantscan.feature.splash.navigation.splashScreen
import com.github.andiim.plantscan.feature.web.navigation.navigateToWebPage
import com.github.andiim.plantscan.feature.web.navigation.webViewScreen

@Composable
fun SetupRootNavGraph(
    appState: PlantScanAppState,
    modifier: Modifier = Modifier,
) {
  val controller = appState.navController
  NavHost(
      modifier = modifier.semantics(false) { contentDescription = "Nav Host" },
      navController = controller,
      startDestination = com.github.andiim.plantscan.feature.splash.navigation.splashRoute,
  ) {
    webViewScreen(
        onBackClick = controller::popBackStack,
    )
    findPlantScreen(
        onManualFindClick = controller::navigateToPlantGraphs,
        onCameraButtonClick = controller::navigateToDetectionsGraph,
        onPlantDetailClick = controller::navigateToDetail)
    myGardenScreen(onDetailClick = controller::navigateToDetail)
    settingsScreen(
        clearAndNavigate = {
          controller.navigate(it) {
            popUpTo(controller.graph.findStartDestination().id) {
              // saveState = true
              inclusive = true
            }
            launchSingleTop = true
            // restoreState = true
          }
        },
        openScreen = { controller.navigate(it) { launchSingleTop = true } },
        nestedGraphs = {
          loginGraphs(
              onWebClicked = controller::navigateToWebPage,
              navigateAndPopUp = { route, popUp ->
                controller.navigate(route) {
                  launchSingleTop = true
                  popUpTo(popUp) { inclusive = true }
                }
              },
          )

          authSignUpScreen(
              navigateAndPopUp = { route, popUp ->
                controller.navigate(route) {
                  launchSingleTop = true
                  popUpTo(popUp) { inclusive = true }
                }
              },
          )
        },
    )

    plantsGraph(
        onBackClick = controller::popBackStack,
        onCategoryClick = controller::navigateToPlantList,
        nestedGraphs = {
          listScreen(
              onItemClick = controller::navigateToDetail,
              onBackClick = controller::popBackStack,
          )
        },
    )

    imagingProcessGraph(
        popUp = controller::popBackStack,
        toDetect = controller::navigateToDetection,
        nestedGraphs = {
          detectScreen(
              popUp = controller::popBackStack,
          )
        },
    )

    detailScreen(
        popUp = controller::popBackStack,
    )

    splashScreen(
        navigateAndPopUp = { route, popup ->
          controller.navigate(route) {
            launchSingleTop = true
            popUpTo(popup) { inclusive = true }
          }
        },
    )
  }
}
