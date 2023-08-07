package com.github.andiim.orchidscan.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocalFlorist
import com.github.andiim.orchidscan.app.R.string as AppText

object NavigationObject {
  val bottomNavItems =
      listOf(
          NavigationItem(
              title = AppText.label_search,
              icon = Icons.Filled.Search,
              direction = Direction.SearchOrDetect),
          NavigationItem(
              title = AppText.label_garden_screen,
              icon = Icons.Outlined.LocalFlorist,
              direction = Direction.MyGarden),
          NavigationItem(
              title = AppText.label_settings,
              icon = Icons.Filled.List,
              direction = Direction.Settings),
      )
}
