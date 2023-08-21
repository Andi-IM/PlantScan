package com.github.andiim.plantscan.feature.camera.navigation

import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.github.andiim.plantscan.feature.camera.databinding.FragmentContainerBinding
import com.github.andiim.plantscan.feature.camera.CameraFragment

private const val DETECTION_GRAPH_ROUTE_PATTERN = "detections_graph"
const val cameraRoute = "camera"
fun NavController.navigateToDetectionsGraph(navOptions: NavOptions? = null) {
  this.navigate(DETECTION_GRAPH_ROUTE_PATTERN, navOptions)
}

fun NavGraphBuilder.imagingProcessGraph(
    popUp: () -> Unit,
    toDetect: ((String) -> Unit)?,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
  navigation(startDestination = cameraRoute, route = DETECTION_GRAPH_ROUTE_PATTERN) {
    composable(route = cameraRoute) {
      AndroidViewBinding(FragmentContainerBinding::inflate) {
        root.getFragment<CameraFragment>().apply {
          onBackPressed = popUp
          toDetectScreen = toDetect
        }
      }
    }
    nestedGraphs()
  }
}
