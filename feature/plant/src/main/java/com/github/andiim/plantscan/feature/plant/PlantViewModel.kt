package com.github.andiim.plantscan.feature.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.data.repository.PlantRepository
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.result.Result
import com.github.andiim.plantscan.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    repository: PlantRepository,
) : ViewModel() {
    val items: StateFlow<PlantUiState> = repository.getPlants()
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> {
                    PlantUiState.Error(result.exception)
                }
                Result.Loading -> PlantUiState.Loading
                is Result.Success -> PlantUiState.Success(result.data)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlantUiState.Loading,
        )
}

sealed interface PlantUiState {
    data class Success(val plants: List<Plant>) : PlantUiState
    data class Error(val throwable: Throwable?) : PlantUiState
    data object Loading : PlantUiState
}
