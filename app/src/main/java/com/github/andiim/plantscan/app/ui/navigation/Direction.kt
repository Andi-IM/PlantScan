package com.github.andiim.plantscan.app.ui.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Direction(val route: String) {
    data object MainNav : Direction("main_navigation")
    data object AccountNav : Direction("account_navigation")
    data object Splash : Direction("splash")
    data object FindPlant : Direction("find")
    data object MyGarden : Direction("my_garden")
    data object Settings : Direction("settings")
    data object Login : Direction("login")
    data object Suggest : Direction("suggest/{plant_id}"){
        fun createRoute(id: String) = "suggest/$id"
    }
    data object List : Direction("plant")
    data object Web : Direction("web_screen/{url}") {
        fun setUrl(url: String) = "web_screen/$url"
    }
    data object Detail : Direction("plant/{plant_id}") {
        fun createRoute(id: String) = "plant/$id"
    }
    data object Camera: Direction("camera")
    data object Detect : Direction("camera/{imageUri}") {
        fun createRoute(imageUri: String) : String {
            val encoder = URLEncoder.encode(imageUri, StandardCharsets.UTF_8.toString())
            return "camera/$encoder"
        }
    }
}
