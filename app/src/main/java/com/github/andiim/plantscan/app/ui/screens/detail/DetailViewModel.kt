package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DetailViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val useCase: PlantUseCase,
    logService: LogService,
) :
    PlantScanViewModel(logService) {

    val detailUiState: StateFlow<DetailUiState> =
        detailUiState(
            plantId = "",
            useCase = useCase
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetailUiState.Loading,
        )
}

private fun detailUiState(
    plantId: String,
    useCase: PlantUseCase,
): Flow<DetailUiState> {
    return useCase.getPlantDetail(plantId).map {
        when (it) {
            is Resource.Error -> DetailUiState.Error
            is Resource.Loading -> DetailUiState.Loading
            is Resource.Success -> {
                val data = it.data
                DetailUiState.Success(data)
            }
        }
    }
}

sealed interface DetailUiState {
    data class Success(val detail: Plant) : DetailUiState
    data object Error : DetailUiState
    data object Loading : DetailUiState
}
