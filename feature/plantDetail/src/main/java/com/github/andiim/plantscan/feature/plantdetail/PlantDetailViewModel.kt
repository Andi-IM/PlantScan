package com.github.andiim.plantscan.feature.plantdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.data.repository.PlantRepository
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.feature.plantdetail.navigation.PlantArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlantDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    plantRepository: PlantRepository,
) : ViewModel() {
    val plantId: String = PlantArgs(savedStateHandle).plantId

    val plantUiState: StateFlow<PlantUiState> = plantRepository.getPlantById(plantId)
        .map(PlantUiState::Success)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PlantUiState.Loading,
        )
}

sealed interface PlantUiState {
    data object Loading : PlantUiState
    data class Success(val data: Plant) : PlantUiState
}
