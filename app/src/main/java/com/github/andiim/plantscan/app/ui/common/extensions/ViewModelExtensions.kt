package com.github.andiim.plantscan.app.ui.common.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarManager
import com.github.andiim.plantscan.app.ui.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun ViewModel.launchCatching(
    logService: LogService,
    snackbar: Boolean = true,
    block: suspend CoroutineScope.() -> Unit
) = viewModelScope.launch(
    CoroutineExceptionHandler { _, throwable ->
        if (snackbar) {
            SnackbarManager.showMessage(throwable.toSnackbarMessage())
        }
        logService.logNonFatalCrash(throwable)
    },
    block = block
)