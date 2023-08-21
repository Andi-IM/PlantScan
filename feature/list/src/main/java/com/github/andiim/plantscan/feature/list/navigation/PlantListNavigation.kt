package com.github.andiim.plantscan.feature.list.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.feature.list.PlantListScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.jetbrains.annotations.VisibleForTesting

private val urlCharacterEncoding = StandardCharsets.UTF_8.name()
@VisibleForTesting internal const val typeArg = "type"

internal class TypeArgs(val type: String) {
  constructor(
      savedStateHandle: SavedStateHandle
  ) : this(URLDecoder.decode(checkNotNull(savedStateHandle[typeArg]), urlCharacterEncoding))
}

fun NavController.navigateToPlantList(type: String) {
  val encodedType = URLEncoder.encode(type, urlCharacterEncoding)
  this.navigate("plants?type=$encodedType") { launchSingleTop = true }
}

fun NavGraphBuilder.listScreen(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
  composable(
      route = "plants?type={${typeArg}}",
      arguments =
          listOf(
              navArgument(typeArg) {
                defaultValue = "all"
                type = NavType.StringType
              },
          ),
  ) {
    PlantListScreen(
        toDetails = onItemClick,
        popUpScreen = onBackClick,
    )
  }
}
