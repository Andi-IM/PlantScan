package com.github.andiim.plantscan.app.ui.screens.camera.composeUtils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.andiim.plantscan.app.R.drawable as AppDrawable
import com.github.andiim.plantscan.app.R.string as AppText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPreviewScreen(
    imageUri: Painter,
    imageDescription: String?,
    onDetect: () -> Unit = {},
    onDispose: () -> Unit = {}
) {
    val cloud = Color(0xFFC5C6D0)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = imageUri,
            contentDescription = imageDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = onDispose,
            modifier = Modifier
                .wrapContentSize()
                .padding(4.dp)
                .background(cloud, CircleShape)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(AppDrawable.ic_close),
                contentDescription = "Close Button",
                tint = Color.White
            )
        }

        Box(
            modifier = Modifier
                .padding(bottom = 6.dp)
                .align(Alignment.BottomCenter)
        ) {
            PlainTooltipBox(tooltip = { Text(stringResource(AppText.camera_detection_tooltip)) }) {
                FloatingActionButton(onClick = onDetect, modifier = Modifier.tooltipTrigger()) {
                    Icon(Icons.Default.CameraEnhance, contentDescription = null)
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview_PhotoPreviewScreen() {
    PhotoPreviewScreen(imageUri = painterResource(AppDrawable.orchid), imageDescription = "")
}
