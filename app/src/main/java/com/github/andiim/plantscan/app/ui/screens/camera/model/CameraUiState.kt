package com.github.andiim.plantscan.app.ui.screens.camera.model

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.extensions.ExtensionMode

/**
 * Defines the current UI state of the camera during pre-capture. The state encapsulates the
 * available camera extensions, the available camera lenses to toggle, the current camera lens, the
 * current extension mode, and the state of the camera.
 */
data class CameraUiState(
    val cameraState: CameraState = CameraState.NOT_READY,
    val availableExtensions: List<Int> = emptyList(),
    val availableCameraLens: List<Int> = listOf(CameraSelector.LENS_FACING_BACK),
    @CameraSelector.LensFacing val cameraLens: Int = CameraSelector.LENS_FACING_BACK,
    @ExtensionMode.Mode val extensionMode: Int = ExtensionMode.NONE,
)

/** Defines the current state of the camera. */
enum class CameraState {
  /** Camera hasn't been initialized. */
  NOT_READY,

  /** Camera is open and presenting a preview stream. */
  READY,

  /** Camera is initialized but the preview has been stopped. */
  PREVIEW_STOPPED
}

/** Defines the various states during post-capture. */
sealed class CaptureState {
  /**
   * Capture capability isn't ready. This could be because the camera isn't initialized, or the
   * camera is changing the lens, or the camera is switching to a new extension mode.
   */
  data object CaptureNotReady : CaptureState()

  /** Capture capability is ready. */
  data object CaptureReady : CaptureState()

  /** Capture process has started. */
  data object CaptureStarted : CaptureState()

  /** Open Image Gallery instead from Capture */
  data object OpenGallery : CaptureState()

  /** Image obtained successfully */
  data class ImageObtained(val uri: Uri?) : CaptureState()

  /** Capture completed successfully. */
  data class CaptureFinished(val outputResults: ImageCapture.OutputFileResults) : CaptureState()

  /** Capture failed with an error. */
  data class CaptureFailed(val exception: Exception) : CaptureState()
}
