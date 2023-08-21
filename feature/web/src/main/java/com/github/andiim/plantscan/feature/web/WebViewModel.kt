package com.github.andiim.plantscan.feature.web

import androidx.lifecycle.SavedStateHandle
import com.github.andiim.plantscan.core.crashlytics.LogService
import com.github.andiim.plantscan.core.crashlytics.PlantScanViewModel
import com.github.andiim.plantscan.feature.web.navigation.WebArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(savedStateHandle: SavedStateHandle, logService: LogService) :
    PlantScanViewModel(logService) {
  private val uriArg: WebArgs =
      WebArgs(
          savedStateHandle,
      )
  val uri = uriArg.webUri
}
