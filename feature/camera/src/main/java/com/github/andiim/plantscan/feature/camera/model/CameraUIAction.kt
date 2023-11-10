package com.github.andiim.plantscan.feature.camera.model

import androidx.camera.core.MeteringPoint
import androidx.camera.extensions.ExtensionMode

sealed class CameraUIAction {
    data object OnCameraClick : CameraUIAction()
    data object OnGalleryViewClick : CameraUIAction()
    data object OnSwitchCameraClick : CameraUIAction()
    data class SelectCameraExtension(@ExtensionMode.Mode val extension: Int) : CameraUIAction()
    data class Scale(val scaleFactor: Float) : CameraUIAction()
    data class Focus(val meteringPoint: MeteringPoint) : CameraUIAction()
}
