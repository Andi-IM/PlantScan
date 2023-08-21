package com.github.andiim.plantscan.feature.detail.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.github.andiim.plantscan.feature.detail.DetailScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8
import org.jetbrains.annotations.VisibleForTesting

private val urlCharacterEncoding = UTF_8.name()
@VisibleForTesting internal const val plantIdArg = "plantId"
private const val DEEP_LINK_URI_PATTERN = "plantscan://app/plants/{$plantIdArg}"

internal class PlantArgs(val plantId: String) {
  constructor(
      savedStateHandle: SavedStateHandle
  ) : this(URLDecoder.decode(checkNotNull(savedStateHandle[plantIdArg]), urlCharacterEncoding))
}

fun NavController.navigateToDetail(plantId: String) {
  val encoder = URLEncoder.encode(plantId, urlCharacterEncoding)
  this.navigate("plants/{$encoder}") { launchSingleTop = true }
}

fun NavGraphBuilder.detailScreen(popUp: () -> Unit) {
  composable(
      route = "plants/{${plantIdArg}}",
      deepLinks =
          listOf(
              navDeepLink { uriPattern = DEEP_LINK_URI_PATTERN },
          ),
      arguments =
          listOf(
              navArgument(plantIdArg) { type = NavType.StringType },
          ),
  ) {
    DetailScreen(onBackClick = popUp)
  }
}
