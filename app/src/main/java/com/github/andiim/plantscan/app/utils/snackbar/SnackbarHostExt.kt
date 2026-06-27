package com.github.andiim.plantscan.app.utils.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

/**
 * Custom Snackbar Message with [SnackbarMessage] visual.
 */
suspend fun SnackbarHostState.showMessage(
    message: String,
    actionLabel: String? = null,
    action: (() -> Unit)? = null,
    duration: SnackbarDuration = SnackbarDuration.Short,
): SnackbarResult {
    val snackbarVisual = SnackbarMessage(
        message = message,
        setDuration = duration,
        action = action,
        actionTitle = actionLabel,
    )
    return this.showSnackbar(snackbarVisual)
}
