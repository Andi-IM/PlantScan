package com.github.andiim.plantscan.feature.camera.component

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.andiim.plantscan.feature.camera.R

@Composable
fun ShutterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                setImageResource(R.drawable.ic_camera_shutter)
                imageTintList = context.getColorStateList(R.color.button)
                setOnClickListener { onClick() }
            }
        },
    )
}
