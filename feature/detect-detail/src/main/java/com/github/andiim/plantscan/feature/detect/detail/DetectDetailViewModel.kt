package com.github.andiim.plantscan.feature.detect.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.domain.GetDetectionDetailsUseCase
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import com.github.andiim.plantscan.feature.detect.detail.navigation.HistoryArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetectDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getDetail: GetDetectionDetailsUseCase,
) : ViewModel() {
    private val historyId = HistoryArgs(savedStateHandle).id
    val detail = getDetail(historyId).map {
        DetectDetailUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = DetectDetailUiState.Loading,
    )
}

sealed interface DetectDetailUiState {
    data object Loading : DetectDetailUiState
    data class Success(val history: DetectionHistory) : DetectDetailUiState
}
