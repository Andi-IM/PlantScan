package com.github.andiim.plantscan.app.ui.common.snackbar

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Short
import com.github.andiim.plantscan.app.R

sealed class SnackbarMessage {
    class StringSnackbar(
        val message: String,
        val duration: SnackbarDuration = Short
    ) : SnackbarMessage()

    class ResourceSnackbar(
        @StringRes val message: Int,
        val duration: SnackbarDuration = Short,
    ) : SnackbarMessage()

    companion object {
        fun SnackbarMessage.toMessage(resources: Resources): String {
            return when (this) {
                is StringSnackbar -> this.message
                is ResourceSnackbar -> resources.getString(this.message)
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
            }
        }

        fun Throwable.toSnackbarMessage(duration: SnackbarDuration = Short): SnackbarMessage {
            val message = this.message.orEmpty()
            return if (message.isNotBlank()) StringSnackbar(message)
            else ResourceSnackbar(R.string.generic_error, duration)
        }
    }
}
