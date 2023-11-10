package com.github.andiim.plantscan.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.domain.GetHistoryUseCase
import com.github.andiim.plantscan.core.domain.GetUserIdUsecase
import com.github.andiim.plantscan.core.result.Result
import com.github.andiim.plantscan.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getHistoryUseCase: GetHistoryUseCase,
    currentUserId: GetUserIdUsecase,
) : ViewModel() {
    val historyUiState = getHistoryUseCase(currentUserId()).asResult().map {
        when (it) {
            Result.Loading -> HistoryUiState.Loading
            is Result.Success -> HistoryUiState.Success(data = it.data)
            is Result.Error -> HistoryUiState.Error(it.exception)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HistoryUiState.Loading,
    )
}
