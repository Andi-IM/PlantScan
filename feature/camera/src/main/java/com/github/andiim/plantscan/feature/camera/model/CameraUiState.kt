package com.github.andiim.plantscan.feature.camera.model

import android.net.Uri
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.extensions.ExtensionMode

/**
 * Defines the current UI state of the camera during pre-capture. The state encapsulates the
 * available camera extensions, the available camera lenses to toggle, the current camera lens, the
 * current extension mode, and the state of the camera.
 */
data class CameraUiState(
    val camera: Camera? = null,
    val cameraState: CameraState = CameraState.NOT_READY,
    val availableExtensions: List<Int> = emptyList(),
    val availableCameraLens: List<Int> = listOf(CameraSelector.LENS_FACING_BACK),
    @CameraSelector.LensFacing val cameraLens: Int = CameraSelector.LENS_FACING_BACK,
    @ExtensionMode.Mode val extensionMode: Int = ExtensionMode.NONE,
)

/**
 * Defines the current state of the camera.
 */
enum class CameraState {
    /** Camera hasn't been initialized. */
    NOT_READY,

    /** Camera is open and presenting a preview stream. */
    READY,

    /** Camera is initialized but the preview has been stopped. */
    PREVIEW_STOPPED
}

sealed class CaptureState {
    data object CaptureNotReady : CaptureState()
    data class ImageObtained(val uri: Uri?) : CaptureState()

    data class CaptureFailed(val exception: Exception) : CaptureState()
}
