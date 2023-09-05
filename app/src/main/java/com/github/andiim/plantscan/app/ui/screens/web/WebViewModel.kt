package com.github.andiim.plantscan.app.ui.screens.web

import androidx.lifecycle.SavedStateHandle
import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    useCase: PlantUseCase,
    savedStateHandle: SavedStateHandle,
    logService: LogService,
) : PlantScanViewModel(logService) {
    // private val webArgs: WebArgs = WebArgs(savedStateHandle)
    // val webUrl = webArgs.webUrl
}