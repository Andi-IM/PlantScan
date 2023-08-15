package com.github.andiim.plantscan.app.detect.model

sealed interface PermissionState {
    object Granted : PermissionState
    data class Denied(val shouldShowRationale: Boolean) : PermissionState
}
