package com.github.andiim.plantscan.feature.web.navigation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.github.andiim.plantscan.feature.web.WebScreen
import java.net.URLDecoder
import org.jetbrains.annotations.VisibleForTesting

private val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

@VisibleForTesting internal const val webArgs = "webUri"

internal class WebArgs(val webUri: String) {
  constructor(
      savedStateHandle: SavedStateHandle
  ) : this(URLDecoder.decode(checkNotNull(savedStateHandle[webArgs]), URL_CHARACTER_ENCODING))
}

fun NavController.navigateToWebPage(webUriAsString: String) {
  val encoder = Uri.encode(webUriAsString)
  this.navigate("web_screen/$encoder")
}

fun NavGraphBuilder.webViewScreen(
    onBackClick: () -> Unit,
) {
  composable(
      route = "web_screen/{$webArgs}",
      arguments = listOf(navArgument(webArgs) { type = NavType.StringType })) {
        WebScreen(popUpScreen = onBackClick)
      }
}
