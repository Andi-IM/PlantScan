package com.github.andiim.orchidscan.app.ui.navigation

import com.github.andiim.orchidscan.app.data.model.Plant

sealed class Direction(val route: String) {
    object MainNav : Direction("main_navigation")
    object AccountNav : Direction("account_navigation")
    object Splash : Direction("splash")
    object FindPlant : Direction("find")
    object Detect : Direction("orchid_detect")
    object MyGarden : Direction("my_garden")
    object Settings : Direction("settings")
    object Login : Direction("login")
    object SignUp : Direction("signup")
    object List : Direction("orchid")
    object Web : Direction("web_screen/{url}") {
        fun setUrl(url: String) = "web_screen/$url"
    }

    object Detail : Direction("orchid/{orchid_id}") {
        fun createRoute(plant: Plant) = "orchid/${plant.id}"
    }
}
