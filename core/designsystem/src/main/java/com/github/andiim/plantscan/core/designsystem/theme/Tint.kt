package com.github.andiim.plantscan.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * A class to model background color of Icon for PlantScan
 */
@Immutable
data class TintTheme(
    val iconTint: Color? = null,
)

/**
 * A composition local for [TintTheme]
 */
val LocalTintTheme: ProvidableCompositionLocal<TintTheme> = staticCompositionLocalOf { TintTheme() }