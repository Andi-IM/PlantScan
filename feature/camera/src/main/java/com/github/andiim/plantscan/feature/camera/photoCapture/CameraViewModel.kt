package com.github.andiim.plantscan.feature.camera.photoCapture

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.MeteringPoint
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.extensions.ExtensionMode
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.network.AppDispatchers
import com.github.andiim.plantscan.core.network.Dispatcher
import com.github.andiim.plantscan.feature.camera.FILENAME
import com.github.andiim.plantscan.feature.camera.PHOTO_EXTENSION
import com.github.andiim.plantscan.feature.camera.R
import com.github.andiim.plantscan.feature.camera.model.CameraState
import com.github.andiim.plantscan.feature.camera.model.CameraUiState
import com.github.andiim.plantscan.feature.camera.model.CaptureState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    @Dispatcher(AppDispatchers.Default) private val defaultDispatcher: CoroutineDispatcher,
    private val provider: ProcessCameraProvider,
    private val manager: ExtensionsManager,
) : ViewModel() {
    private var resolver: ContentResolver? = null
    var camera: Camera? = null
    private val resolution = ResolutionSelector.Builder()
        .setAllowedResolutionMode(
            ResolutionSelector.PREFER_CAPTURE_RATE_OVER_HIGHER_RESOLUTION,
        )
        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
        .build()

    private val imageCapture: ImageCapture =
        ImageCapture.Builder().setResolutionSelector(resolution).build()
    private val preview =
        Preview.Builder().setResolutionSelector(resolution).build()

    private val _uiState: MutableStateFlow<CameraUiState> = MutableStateFlow(CameraUiState())
    private val _captureState: MutableStateFlow<CaptureState> =
        MutableStateFlow(CaptureState.CaptureNotReady)

    val uiState = _uiState.asStateFlow()
    val captureState = _captureState.asStateFlow()

    /**
     * Initializes the camera and checks which extensions are available for the selected camera lens
     * face. If no extensions are available then the selected extension will be set to None and the
     * available extensions list will also contain None. Because this operation is async, clients
     * should wait for uiState to emit CameraState.READY. Once the camera is ready the client
     * can start the preview.
     */
    fun initializeCamera() {
        viewModelScope.launch {
            val currentUiState = _uiState.value

            // get the camera selector for the select lens face
            val cameraSelector = cameraLensToSelector(currentUiState.cameraLens)
            val availableCameraLens =
                listOf(
                    CameraSelector.LENS_FACING_BACK,
                    CameraSelector.LENS_FACING_FRONT,
                ).filter { lensFacing ->
                    provider.hasCamera(cameraLensToSelector(lensFacing))
                }

            // get the supported extensions for the selected camera lens by filtering the full list
            // of extensions and checking each one if it's available
            val availableExtensions =
                listOf(
                    ExtensionMode.AUTO,
                    ExtensionMode.BOKEH,
                    ExtensionMode.HDR,
                    ExtensionMode.NIGHT,
                    ExtensionMode.FACE_RETOUCH,
                )
                    .filter { extensionMode ->
                        manager.isExtensionAvailable(cameraSelector, extensionMode)
                    }

            // prepare the new camera UI state which is now in the READY state and contains the list
            // of available extensions, available lens faces.
            val newuiState =
                currentUiState.copy(
                    cameraState = CameraState.READY,
                    availableExtensions = listOf(ExtensionMode.NONE) + availableExtensions,
                    availableCameraLens = availableCameraLens,
                    extensionMode =
                    if (availableExtensions.isEmpty()) {
                        ExtensionMode.NONE
                    } else {
                        currentUiState.extensionMode
                    },
                )
            _uiState.emit(newuiState)
        }
    }

    /**
     * Starts the preview stream. The camera state should be in the READY or PREVIEW_STOPPED state
     * when calling this operation. This process will bind the preview and image capture uses cases to
     * the camera provider.
     */
    fun startPreview(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        val currentCameraUiState = _uiState.value
        val cameraSelector =
            if (currentCameraUiState.extensionMode == ExtensionMode.NONE) {
                cameraLensToSelector(currentCameraUiState.cameraLens)
            } else {
                manager.getExtensionEnabledCameraSelector(
                    cameraLensToSelector(currentCameraUiState.cameraLens),
                    currentCameraUiState.extensionMode,
                )
            }
        val useCaseGroup =
            UseCaseGroup.Builder()
                .setViewPort(previewView.viewPort!!)
                .addUseCase(imageCapture)
                .addUseCase(preview)
                .build()
        provider.unbindAll()
        camera = provider.bindToLifecycle(lifecycleOwner, cameraSelector, useCaseGroup)
        preview.setSurfaceProvider(previewView.surfaceProvider)

        _uiState.value = _uiState.value.copy(camera = camera, cameraState = CameraState.READY)
    }

    fun switchCamera() {
        val currentCameraUiState = _uiState.value
        if (currentCameraUiState.cameraState == CameraState.READY) {
            // To switch the camera lens, there has to be at least 2 camera lenses
            if (currentCameraUiState.availableCameraLens.size == 1) return

            val camLensFacing = currentCameraUiState.cameraLens
            // Toggle the lens facing
            val newCameraUiState =
                if (camLensFacing == CameraSelector.LENS_FACING_BACK) {
                    currentCameraUiState.copy(cameraLens = CameraSelector.LENS_FACING_FRONT)
                } else {
                    currentCameraUiState.copy(cameraLens = CameraSelector.LENS_FACING_BACK)
                }

            _uiState.value = newCameraUiState.copy(
                cameraState = CameraState.NOT_READY,
            )

            _captureState.value = CaptureState.CaptureNotReady
        }
    }

    /**
     * Captures the photo and saves it to the pictures directory that's inside the app-specific
     * directory on external storage. Upon successful capture, the captureUiState flow will emit
     * CaptureFinished with the URI to the captured photo. If the capture operation failed then
     * captureUiState flow will emit CaptureFailed with the exception containing more details on the
     * reason for failure.
     */
    fun capturePhoto(context: Context) {
        val metadata =
            ImageCapture.Metadata().apply {
                // Mirror image when using the front camera
                isReversedHorizontal =
                    _uiState.value.cameraLens == CameraSelector.LENS_FACING_FRONT
            }
        resolver = context.contentResolver
        val dirname = context.getString(R.string.app_name)
        val outputFileOptions =
            getOutputFileOptions(dirname, resolver!!, metadata) // resolver is exactly created!
        imageCapture.takePicture(
            outputFileOptions,
            defaultDispatcher.asExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri
                    _captureState.value = CaptureState.ImageObtained(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    _captureState.value = CaptureState.CaptureFailed(exception)
                }
            },
        )
    }

    private fun getOutputFileOptions(
        dirname: String,
        resolver: ContentResolver,
        metadata: ImageCapture.Metadata,
    ): ImageCapture.OutputFileOptions {
        val nowTimeStamp: Long = System.currentTimeMillis()
        val imageName = SimpleDateFormat(FILENAME, Locale.US).format(nowTimeStamp)
        val saveCollection: Uri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY,
            )

            else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val contentValues: ContentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$imageName$PHOTO_EXTENSION")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.DATE_TAKEN, nowTimeStamp)
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_DCIM + "/" + dirname,
                )
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                put(MediaStore.Images.Media.DATE_TAKEN, nowTimeStamp)
                put(MediaStore.Images.Media.DATE_ADDED, nowTimeStamp)
                put(MediaStore.Images.Media.DATE_MODIFIED, nowTimeStamp)
                put(MediaStore.Images.Media.AUTHOR, "Your Name")
                put(MediaStore.Images.Media.DESCRIPTION, "Your description")
            }
        }
        return ImageCapture.OutputFileOptions.Builder(
            resolver,
            saveCollection,
            contentValues,
        ).setMetadata(metadata).build()
    }

    /** Sets the current extension mode. This will force the camera to rebind the use cases. */
    fun setExtensionMode(@ExtensionMode.Mode extensionMode: Int) {
        _uiState.value = _uiState.value.copy(
            cameraState = CameraState.NOT_READY,
            extensionMode = extensionMode,
        )
    }

    fun focus(meteringPoint: MeteringPoint) {
        val camera = camera ?: return

        val meteringAction = FocusMeteringAction.Builder(meteringPoint).build()
        camera.cameraControl.startFocusAndMetering(meteringAction)
    }

    fun scale(scaleFactor: Float) {
        val camera = camera ?: return
        val currentZoomRatio: Float = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
        camera.cameraControl.setZoomRatio(scaleFactor * currentZoomRatio)
    }

    private fun cameraLensToSelector(@CameraSelector.LensFacing lensFacing: Int): CameraSelector =
        when (lensFacing) {
            CameraSelector.LENS_FACING_FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
            CameraSelector.LENS_FACING_BACK -> CameraSelector.DEFAULT_BACK_CAMERA
            else -> throw IllegalArgumentException("Invalid lens facing type: $lensFacing")
        }

    fun resetCaptureState() {
        _captureState.value = CaptureState.CaptureNotReady
    }
}
