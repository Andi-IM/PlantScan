package com.github.andiim.orchidscan.app.ui.navigation

sealed class Direction(val route: String) {
  object MainNav : Direction("main_navigation")
  object AccountNav : Direction("account_navigation")
  object Splash : Direction("splash")
  object SearchOrDetect : Direction("search_or_detect")
  object Search : Direction("search")
  object Detect : Direction("orchid_detect")
  object MyGarden : Direction("my_garden")
  object Settings : Direction("settings")
  object Login : Direction("login")
  object SignUp : Direction("signup")
  object OrchidList : Direction("orchid")
  object WebScreenView : Direction("web_screen/{url}") {
    fun setUrl(url: String) = "web_screen/$url"
  }
  object OrchidDetail : Direction("orchid/{orchid_id}") {
    // TODO : Create Plant
  }
}
