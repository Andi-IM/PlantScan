package com.github.andiim.plantscan.app.ui.screens.home.history

import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject constructor(useCase: PlantUseCase, logService: LogService) :
    PlantScanViewModel(logService) {

}

private fun historyUiState() {

}

sealed interface HistoryUiState {
    data object Success : HistoryUiState
    data object Error : HistoryUiState
    data object Loading : HistoryUiState

}