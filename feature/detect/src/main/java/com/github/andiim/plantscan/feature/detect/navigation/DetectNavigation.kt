package com.github.andiim.plantscan.feature.detect.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.feature.detect.DetectScreen
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8
import org.jetbrains.annotations.VisibleForTesting

private val urlCharacterEncoding = UTF_8.name()
@VisibleForTesting internal const val imageUriArg = "imageUri"

internal class ImageUriArgs(val imageUri: String) {
  constructor(
      savedStateHandle: SavedStateHandle
  ) : this(URLDecoder.decode(checkNotNull(savedStateHandle[imageUriArg]), urlCharacterEncoding))
}

fun NavController.navigateToDetection(imageUri: String) {
  val encoder = URLEncoder.encode(imageUri, urlCharacterEncoding)
  this.navigate("camera/$encoder") { launchSingleTop = true }
}

fun NavGraphBuilder.detectScreen(popUp: () -> Unit) {
  composable(
      route = "camera/${imageUriArg}",
      arguments =
          listOf(
              navArgument(imageUriArg) { type = NavType.StringType },
          ),
  ) {
    DetectScreen(onBack = popUp)
  }
}
