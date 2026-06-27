package com.github.andiim.plantscan.feature.camera.component

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.andiim.plantscan.core.designsystem.extensions.withSemantics
import com.github.andiim.plantscan.core.designsystem.theme.PsTheme
import com.github.andiim.plantscan.feature.camera.R

private const val HALF_CLOCKWISE = 180f
private const val ANIMATION_DURATION = 300L
private const val INITIAL_ROTATION = 0f

@Composable
fun ChangeCameraBtn(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ctx = LocalContext.current
    AndroidView(
        modifier = modifier.withSemantics(ctx.getString(R.string.flip_camera)),
        factory = { context ->
            ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                setImageResource(R.drawable.ic_flip_camera_android)
                imageTintList = context.getColorStateList(R.color.button)
            }.also { imageView ->
                imageView.setOnClickListener {
                    imageView.animate().apply {
                        rotation(-HALF_CLOCKWISE)
                        duration = ANIMATION_DURATION
                        setListener(
                            object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    imageView.rotation = INITIAL_ROTATION
                                }
                            },
                        )
                    }
                    onClick()
                }
            }
        },
    )
}

@Preview
@Composable
fun RotatingIconButtonPreview() {
    PsTheme {
        ChangeCameraBtn(
            modifier = Modifier.size(64.dp),
            onClick = {},
        )
    }
}
