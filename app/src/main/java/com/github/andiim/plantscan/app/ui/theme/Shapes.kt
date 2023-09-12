package com.github.andiim.plantscan.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

data class Shape(
    val default: RoundedCornerShape = RoundedCornerShape(0.dp),
    val small: RoundedCornerShape = RoundedCornerShape(4.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(8.dp),
    val large: RoundedCornerShape = RoundedCornerShape(16.dp)
)

// val LocalShape = compositionLocalOf { Shape() }

// val MaterialTheme.shapeScheme: Shape
//  @Composable @ReadOnlyComposable get() = LocalShape.current
