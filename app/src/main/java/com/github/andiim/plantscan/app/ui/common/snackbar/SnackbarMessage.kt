package com.github.andiim.plantscan.app.ui.common.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Short
import com.github.andiim.plantscan.app.R

sealed class SnackbarMessage {
    class StringSnackbar(
        val message: String,
        val duration: SnackbarDuration = Short,
        val action: (() -> Unit)? = null
    ) : SnackbarMessage()

    class SnackbarWithLabel(
        val message: String,
        val duration: SnackbarDuration = Short,
        val label: String?,
        val isError: Boolean,
        val action: (() -> Unit)? = null
    ) : SnackbarMessage()

    class ResourceSnackbar(
        @StringRes val message: Int,
        val duration: SnackbarDuration = Short,
        val action: (() -> Unit)? = null
    ) : SnackbarMessage()

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String? {
            return when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> resources.getString(this.message)
                else -> null
            }
        }

        /**
         * Return some duration if specified.
         *
         * @return [SnackbarDuration.Short] if not specified.
         */
        fun SnackbarMessage.getDuration(): SnackbarDuration {
            return when (this) {
                is StringSnackbar -> this.duration
                is ResourceSnackbar -> this.duration
                is SnackbarWithLabel -> this.duration
            }
        }

        fun SnackbarMessage.getAction(): (() -> Unit)? {
            return when (this) {
                is ResourceSnackbar -> this.action
                is StringSnackbar -> this.action
                is SnackbarWithLabel -> this.action
            }
        }

        fun Throwable.toSnackbarMessage(duration: SnackbarDuration = Short): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) {
                StringSnackbar(message, duration)
            } else {
                ResourceSnackbar(R.string.generic_error, duration)
            }
        }
    }
}
