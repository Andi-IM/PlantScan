package com.github.andiim.plantscan.feature.camera.component

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

private const val SPRING_STIFFNESS_ALPHA_OUT = 100f
private const val SPRING_STIFFNESS = 800f
private const val SPRING_DAMPING_RATIO = 0.35f
private const val SCALE_X = 1.5f
private const val SCALE_Y = 1.5f

@Composable
fun FocusPoint(
    x: Float,
    y: Float,
) {
    AndroidView(
        factory = { context ->
            View(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            }.also { view ->
                val drawable = FocusPointDrawable()
                val strokeWidth =
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        3f,
                        context.resources.displayMetrics,
                    )
                drawable.setStrokeWidth(strokeWidth)

                val alphaAnimation =
                    SpringAnimation(view, DynamicAnimation.ALPHA, 1f).apply {
                        spring.stiffness = SPRING_STIFFNESS
                        spring.dampingRatio = SPRING_DAMPING_RATIO

                        addEndListener { _, _, _, _ ->
                            SpringAnimation(view, DynamicAnimation.ALPHA, 0f)
                                .apply {
                                    spring.stiffness = SPRING_STIFFNESS_ALPHA_OUT
                                    spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
                                }
                                .start()
                        }
                    }
                val scaleAnimationX =
                    SpringAnimation(view, DynamicAnimation.SCALE_X, 1f).apply {
                        spring.stiffness = SPRING_STIFFNESS
                        spring.dampingRatio = SPRING_DAMPING_RATIO
                    }
                val scaleAnimationY =
                    SpringAnimation(view, DynamicAnimation.SCALE_Y, 1f).apply {
                        spring.stiffness = SPRING_STIFFNESS
                        spring.dampingRatio = SPRING_DAMPING_RATIO
                    }

                with(view) {
                    background = drawable
                    isVisible = true
                    translationX = x - width / 2f
                    translationY = y - height / 2f
                    alpha = 0f
                    scaleX = SCALE_X
                    scaleY = SCALE_Y
                }

                alphaAnimation.start()
                scaleAnimationX.start()
                scaleAnimationY.start()
            }
        },
    )
}
