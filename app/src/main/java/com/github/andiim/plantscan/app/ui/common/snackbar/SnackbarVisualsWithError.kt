package com.github.andiim.plantscan.app.ui.common.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

class SnackbarVisualsWithError(
    override val message: String,
    val setDuration: SnackbarDuration,
    val isError: Boolean,
    val actionTitle: String? = null,
    val action: (()-> Unit)? = null
) : SnackbarVisuals {
    override val actionLabel: String?
        get() = if (isError) actionTitle else null
    override val duration: SnackbarDuration = setDuration
    override val withDismissAction: Boolean = false


}