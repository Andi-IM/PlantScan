package com.github.andiim.plantscan.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.core.designsystem.theme.LocalBackgroundtheme
import com.github.andiim.plantscan.core.designsystem.theme.PlantScanTheme

/**
 * The main background for the app.
 * Uses [LocalBackgroundtheme] to set the color and tonal elevation of a [Surface].
 *
 * @param modifier Modifier to be applied to the background.
 * @param content The background content.
 */
@Composable
fun PlantScanBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val color = LocalBackgroundtheme.current.color
    val tonalElevation = LocalBackgroundtheme.current.tonalElevation
    Surface(
        color = if (color == Color.Unspecified) Color.Transparent else color,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = modifier.fillMaxSize(),
    ) {
        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            content()
        }
    }
}

/**
 * Multipreview annotation that represents light and dark themes. Add this annotation to a
 * composable to render the both themes.
 */
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreviews

@ThemePreviews
@Composable
fun BackgroundDefault() {
    PlantScanTheme(disableDynamicTheming = true) {
        PlantScanBackground(Modifier.size(100.dp), content = {})
    }
}

@ThemePreviews
@Composable
fun BackgroundDynamic() {
    PlantScanTheme(disableDynamicTheming = false) {
        PlantScanBackground(Modifier.size(100.dp), content = {})
    }
}
