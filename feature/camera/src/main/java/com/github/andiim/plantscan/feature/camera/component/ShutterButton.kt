package com.github.andiim.plantscan.feature.camera.component

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

@Composable
fun ShutterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val ctx = LocalContext.current
    AndroidView(
        modifier = modifier.withSemantics(ctx.getString(R.string.shutter)),
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

@Preview
@Composable
fun ShutterButton_Preview() {
    PsTheme {
        ShutterButton(
            modifier = Modifier.size(64.dp),
            onClick = {},
        )
    }
}
