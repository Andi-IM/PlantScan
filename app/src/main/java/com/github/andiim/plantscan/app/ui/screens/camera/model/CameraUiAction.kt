package com.github.andiim.plantscan.app.ui.screens.camera.model

import androidx.camera.core.MeteringPoint
import androidx.camera.extensions.ExtensionMode

/** User initiated actions related to camera operations. */
sealed class CameraUiAction {
    data object RequestPermissionClick : CameraUiAction()
    data object SwitchCameraClick : CameraUiAction()
    data object AddFromGalleryClick : CameraUiAction()
    data object ShutterButtonClick : CameraUiAction()
    data object ClosePhotoPreviewClick : CameraUiAction()
    data object CloseCameraClick : CameraUiAction()
    data class ActionDetect(val uri: String) : CameraUiAction()
    data class SelectCameraExtension(@ExtensionMode.Mode val extension: Int) : CameraUiAction()
    data class Focus(val meteringPoint: MeteringPoint) : CameraUiAction()
    data class Scale(val scaleFactor: Float) : CameraUiAction()
}
