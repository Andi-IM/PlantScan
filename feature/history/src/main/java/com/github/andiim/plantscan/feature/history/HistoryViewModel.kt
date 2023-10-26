package com.github.andiim.plantscan.feature.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(

) : ViewModel() {
}

sealed interface HistoryUiState {
    data object Loading : HistoryUiState
    data class Error(val throwable: Throwable) : HistoryUiState
    data class Success(val data: List<String>) : HistoryUiState
}