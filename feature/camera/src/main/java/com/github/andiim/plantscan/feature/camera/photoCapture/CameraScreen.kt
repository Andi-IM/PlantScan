package com.github.andiim.plantscan.feature.camera.photoCapture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.feature.camera.rotateBitmap
import timber.log.Timber
import java.util.concurrent.Executor

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
) {
    val cameraState: CameraState by viewModel.state.collectAsStateWithLifecycle()
    CameraContent(
        onPhotoCaptured = viewModel::storePhotoToGallery,
        lastCapturedPhoto = cameraState.capturedImage,
    )
}

@Composable
fun CameraContent(
    onPhotoCaptured: (Bitmap) -> Unit,
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap? = null,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    capturePhoto(context, cameraController, onPhotoCaptured)
                },
            ) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera capture icon")
                Text(text = "TakePhoto")
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier
                    .matchParentSize()
                    .padding(paddingValues),
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        setBackgroundColor(Color.BLACK)
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                },
            )

            if (lastCapturedPhoto != null) {
                LastPhotoPreview(
                    modifier = Modifier.align(alignment = Alignment.BottomStart),
                    lastCapturedPhoto = lastCapturedPhoto,
                )
            }
        }
    }
}

fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Bitmap) -> Unit,
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)
    cameraController.takePicture(
        mainExecutor,
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val correctedBitmap: Bitmap = image
                    .toBitmap()
                    .rotateBitmap(image.imageInfo.rotationDegrees)

                onPhotoCaptured(correctedBitmap)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                Timber.tag("CameraContent").e(exception, "Error capturing image")
            }
        },
    )
}

@Composable
fun LastPhotoPreview(
    lastCapturedPhoto: Bitmap,
    modifier: Modifier = Modifier,
) {
    val capturedPhoto: ImageBitmap =
        remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }

    Card(
        modifier = modifier
            .size(128.dp)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.large,
    ) {
        Image(
            bitmap = capturedPhoto,
            contentDescription = "Last captured photo",
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
fun Preview_CameraContent() {
    CameraContent(onPhotoCaptured = {})
}
