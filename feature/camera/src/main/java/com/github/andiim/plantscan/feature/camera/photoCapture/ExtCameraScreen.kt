package com.github.andiim.plantscan.feature.camera.photoCapture

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector.LENS_FACING_BACK
import androidx.camera.core.CameraSelector.LENS_FACING_FRONT
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.github.andiim.plantscan.feature.camera.component.CameraPreviewView
import com.github.andiim.plantscan.feature.camera.component.CameraUIAction
import com.github.andiim.plantscan.feature.camera.extensions.takePicture

@Composable
fun ExtCameraView(
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit,
) {
    val context = LocalContext.current
    var lensFacing by remember { mutableIntStateOf(LENS_FACING_BACK) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        if (uri != null) onImageCaptured(uri, true)
    }
    CameraPreviewView(imageCapture = imageCapture) {
        when (it) {
            CameraUIAction.OnCameraClick -> {
                imageCapture.takePicture(context, lensFacing, onImageCaptured, onError)
            }
            CameraUIAction.OnGalleryViewClick -> {
                galleryLauncher.launch("image/*")
            }
            CameraUIAction.OnSwitchCameraClick -> {
                lensFacing =
                    if (lensFacing == LENS_FACING_BACK) {
                        LENS_FACING_FRONT
                    } else {
                        LENS_FACING_BACK
                    }
            }
        }
    }
}
