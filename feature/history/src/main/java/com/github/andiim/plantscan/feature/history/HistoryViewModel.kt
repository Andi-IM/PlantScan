package com.github.andiim.plantscan.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.domain.GetHistoryUseCase
import com.github.andiim.plantscan.core.result.Result
import com.github.andiim.plantscan.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    historyUseCase: GetHistoryUseCase,
) : ViewModel() {
    /**
     * TODO: Retrieve user id.
     */
    val historyUiState: StateFlow<HistoryUiState> =
        historyUseCase("").asResult().map {
            when (it) {
                is Result.Error -> HistoryUiState.Error(it.exception)
                Result.Loading -> HistoryUiState.Loading
                is Result.Success -> HistoryUiState.Success(data = it.data)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoryUiState.Loading,
        )
}
