package com.github.andiim.plantscan.app.ui.navigation

import com.github.andiim.plantscan.app.core.domain.model.Plant
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Direction(val route: String) {
    object MainNav : Direction("main_navigation")
    object AccountNav : Direction("account_navigation")
    object Splash : Direction("splash")
    object FindPlant : Direction("find")
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

    object Camera: Direction("camera")

    object Detect : Direction("camera/{imageUri}") {
        fun createRoute(imageUri: String) : String {
            val encoder = URLEncoder.encode(imageUri, StandardCharsets.UTF_8.toString())
            return "camera/$encoder"
        }
    }
}
