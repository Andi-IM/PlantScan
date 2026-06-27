package com.github.andiim.plantscan.app.utils.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Short
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SnackbarManager {
    private val messages: MutableStateFlow<SnackbarMessage?> = MutableStateFlow(null)
    val snackbarMessages: StateFlow<SnackbarMessage?>
        get() = messages.asStateFlow()

    fun showMessage(
        message: String,
        action: (() -> Unit)? = null,
        duration: SnackbarDuration = Short,
        label: String? = null,
    ) {
        messages.value = SnackbarMessage(
            message = message,
            setDuration = duration,
            actionTitle = label,
            action = action
        )
    }
}
