package com.github.andiim.orchidscan.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(@StringRes val title: Int, val icon: ImageVector, val direction: Direction)
