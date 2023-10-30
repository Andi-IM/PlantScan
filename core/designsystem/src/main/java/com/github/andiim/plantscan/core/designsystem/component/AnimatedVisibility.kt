package com.github.andiim.plantscan.core.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable

@Composable
fun PsAnimatedVisibility(
    visible: Boolean,
    animatedData: PsAnimatedVisibilityData = PsAnimatedVisibilityData.Default,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) {
    val enter = when (animatedData) {
        PsAnimatedVisibilityData.BottomBar -> animatedData.enter
        PsAnimatedVisibilityData.Default -> animatedData.enter
    }
    val exit = when (animatedData) {
        PsAnimatedVisibilityData.BottomBar -> animatedData.exit
        PsAnimatedVisibilityData.Default -> animatedData.exit
    }
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit,
        content = content,
    )
}

enum class PsAnimatedVisibilityData(
    val enter: EnterTransition = fadeIn() + expandIn(),
    val exit: ExitTransition = shrinkOut() + fadeOut(),
) {
    BottomBar(
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> 2 * fullHeight },
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> 2 * fullHeight },
        ),
    ),
    Default(),
}
