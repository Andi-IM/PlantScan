package com.github.andiim.plantscan.feature.camera.component

import android.util.TypedValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.github.andiim.plantscan.core.designsystem.component.PsBackground
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import kotlin.math.min

@Composable
fun FocusPoint(
    scale: Float,
    alpha: Float,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val strokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            3f,
            context.resources.displayMetrics,
        )
        val radius = (min(canvasWidth, canvasHeight) / 2) - (strokeWidth / 2)
        scale(scale = scale, pivot = Offset(0f, 0f)) {
            drawCircle(
                color = Color.White,
                radius = radius,
                style = Stroke(
                    width = strokeWidth,
                ),
                alpha = alpha,
                center = Offset(0f, 0f),
            )
        }
    }
}

@Preview
@Composable
fun FocusPoint_Preview() {
    PsTheme(darkTheme = true) {
        PsBackground {
            FocusPoint(scale = 1f, alpha = 1f)
        }
    }
}
