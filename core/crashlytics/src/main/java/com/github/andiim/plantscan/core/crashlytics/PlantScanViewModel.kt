package com.github.andiim.plantscan.core.crashlytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.ui.snackbar.SnackbarManager
import com.github.andiim.plantscan.core.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class PlantScanViewModel(private val logService: LogService) : ViewModel() {
  fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
      viewModelScope.launch(
          CoroutineExceptionHandler { _, throwable ->
            if (snackbar) {
              SnackbarManager.showMessage(throwable.toSnackbarMessage())
            }
            logService.logNonFatalCrash(throwable)
          },
          block = block)
}
