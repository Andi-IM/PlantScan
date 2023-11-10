package com.github.andiim.plantscan.feature.camera.photoCapture

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.camera.core.MeteringPoint
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.andiim.plantscan.core.designsystem.icon.PsIcons
import com.github.andiim.plantscan.core.ui.TrackScreenViewEvent
import com.github.andiim.plantscan.feature.camera.component.CameraPreviewView
import com.github.andiim.plantscan.feature.camera.model.CameraUIAction
import com.github.andiim.plantscan.feature.camera.model.CameraUiState
import com.github.andiim.plantscan.feature.camera.model.CaptureState
import com.github.andiim.plantscan.feature.camera.noPermission.NoPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraRoute(
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
    onImageCaptured: (String) -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val cameraPermissionState: PermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var galleryLauncherOpened by remember { mutableStateOf(false) }
    val galleryLauncher =
        rememberLauncherForActivityResult(PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                context.contentResolver.takePersistableUriPermission(uri, flag)
                onImageCaptured(uri.toString())
            }
        }

    val cameraState: CameraUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val captureState by viewModel.captureState.collectAsStateWithLifecycle()

    LaunchedEffect(galleryLauncherOpened) {
        if (galleryLauncherOpened) {
            delay(DELAY_MILLIS)
            galleryLauncher
                .launch(PickVisualMediaRequest(ImageOnly))
                .also {
                    galleryLauncherOpened = false
                }
        }
    }

    if (cameraPermissionState.status.isGranted) {
        SideEffect {
            viewModel.initializeCamera()
        }
        CameraScreen(
            onBackPressed = onBackClick,
            cameraUiState = cameraState,
            captureState = captureState,
            onCameraStartPreview = viewModel::startPreview,
            onCameraSwitch = viewModel::switchCamera,
            onCameraCapture = viewModel::capturePhoto,
            onCameraZoom = viewModel::scale,
            onCameraFocus = viewModel::focus,
            onModeChanged = viewModel::setExtensionMode,
            onImageCaptured = { uri ->
                onImageCaptured(uri)
                viewModel.resetCaptureState()
            },
            onShowSnackbar = onShowSnackbar,
            onGalleryLauncherOpened = {
                galleryLauncherOpened = true
            },
        )
    } else {
        NoPermissionScreen(
            shouldShowRationale = cameraPermissionState.status.shouldShowRationale,
            onBackClick = onBackClick,
            onGalleryLauncherOpened = { galleryLauncherOpened = true },
            onRequestPermission = {
                cameraPermissionState.launchPermissionRequest()
            },
        )
    }
}

private const val DELAY_MILLIS = 700L

@Suppress("detekt:LongParameterList", "LongMethod")
@Composable
fun CameraScreen(
    cameraUiState: CameraUiState,
    captureState: CaptureState,
    onBackPressed: () -> Unit,
    onCameraStartPreview: (LifecycleOwner, PreviewView) -> Unit,
    onCameraSwitch: () -> Unit,
    onCameraCapture: (Context) -> Unit,
    onCameraZoom: (Float) -> Unit,
    onCameraFocus: (MeteringPoint) -> Unit,
    onModeChanged: (Int) -> Unit,
    onImageCaptured: (String) -> Unit,
    onGalleryLauncherOpened: () -> Unit,
    onShowSnackbar: suspend (String, String?, SnackbarDuration?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    TrackScreenViewEvent(screenName = "Camera")
    LaunchedEffect(captureState) {
        when (captureState) {
            CaptureState.CaptureNotReady -> Unit
            is CaptureState.ImageObtained -> {
                captureState.uri?.let {
                    onImageCaptured(it.toString())
                }
            }

            is CaptureState.CaptureFailed -> {
                scope.launch {
                    onShowSnackbar(
                        captureState.toString(),
                        null,
                        SnackbarDuration.Short,
                    )
                }
            }
        }
    }

    Box(modifier = modifier) {
        CameraPreviewView(
            camera = cameraUiState.camera,
            lensFacing = cameraUiState.cameraLens,
            extensionMode = cameraUiState.extensionMode,
            availableExtensions = cameraUiState.availableExtensions,
            onPreviewStart = onCameraStartPreview,
        ) { cameraUIAction ->
            when (cameraUIAction) {
                is CameraUIAction.OnCameraClick -> {
                    onCameraCapture(context)
                }

                is CameraUIAction.OnSwitchCameraClick -> onCameraSwitch.invoke()
                is CameraUIAction.OnGalleryViewClick -> onGalleryLauncherOpened.invoke()

                is CameraUIAction.Scale -> {
                    onCameraZoom(cameraUIAction.scaleFactor)
                }

                is CameraUIAction.Focus -> {
                    onCameraFocus(cameraUIAction.meteringPoint)
                }

                is CameraUIAction.SelectCameraExtension -> {
                    onModeChanged(cameraUIAction.extension)
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.TopStart),
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            IconButton(
                onClick = onBackPressed,
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape,
                    ),
            ) {
                Icon(PsIcons.Back, null, tint = Color.Black)
            }
        }
    }
}
