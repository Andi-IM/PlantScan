package com.github.andiim.plantscan.app.utils.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

class SnackbarMessage(
    override val message: String,
    val setDuration: SnackbarDuration,
    val actionTitle: String? = null,
    val action: (() -> Unit)? = null,
) : SnackbarVisuals {
    override val actionLabel: String? = actionTitle
    override val duration: SnackbarDuration = setDuration
    override val withDismissAction: Boolean = false
}
