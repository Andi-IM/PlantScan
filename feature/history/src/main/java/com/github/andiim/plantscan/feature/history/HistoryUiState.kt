package com.github.andiim.plantscan.feature.history

import com.github.andiim.plantscan.core.model.data.DetectionHistory

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Error(val throwable: Throwable?) : HistoryUiState
    data class Success(
        val data: List<DetectionHistory> = emptyList(),
    ) : HistoryUiState
}
