package com.github.andiim.plantscan.core.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.github.andiim.plantscan.core.designsystem.R.drawable as dsDrawable
import com.github.andiim.plantscan.core.ui.R.string as uiString

@Composable
fun DetectImage(imageData: Any, onImageClick: () -> Unit) {
    val model = ImageRequest.Builder(LocalContext.current)
        .data(imageData)
        .crossfade(true).build()
    DetectImage(model, onImageClick)
}

@Composable
private fun DetectImage(
    model: ImageRequest,
    onImageClick: () -> Unit,
) {
    SubcomposeAsyncImage(
        model = model,
        loading = {
            if (LocalInspectionMode.current) {
                ImageInspection()
            } else {
                Box(modifier = Modifier.height(30.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.width(24.dp))
                }
            }
        },
        error = {
            Box(modifier = Modifier.height(30.dp), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Dangerous,
                    contentDescription = stringResource(uiString.fetch_image_error),
                )
            }
        },
        contentDescription = "image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .animateContentSize()
            .clickable { onImageClick() },
    )
}

@Composable
fun ImageInspection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(dsDrawable.orchid),
            contentScale = ContentScale.FillHeight,
            contentDescription = null,
        )
    }
}
