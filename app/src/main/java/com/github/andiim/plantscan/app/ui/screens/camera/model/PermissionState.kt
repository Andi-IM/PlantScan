package com.github.andiim.plantscan.app.ui.screens.camera.model

sealed interface PermissionState {
    object Granted : PermissionState
    data class Denied(val shouldShowRationale: Boolean) : PermissionState
}
