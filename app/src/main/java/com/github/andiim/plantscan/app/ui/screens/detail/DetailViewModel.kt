package com.github.andiim.plantscan.app.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    useCase: PlantUseCase
) : ViewModel() {

    private val detailArgs: DetailArgs = DetailArgs(savedStateHandle)
    val plantId = detailArgs.plantId

    val detailUiState: StateFlow<DetailUiState> =
        useCase.getPlantDetail(plantId)
            .map {
                when (it) {
                    is Resource.Error -> DetailUiState.Error
                    is Resource.Loading -> DetailUiState.Loading
                    is Resource.Success -> {
                        val data = it.data
                        DetailUiState.Success(data)
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = DetailUiState.Loading,
            )
}

sealed interface DetailUiState {
    data class Success(val detail: Plant) : DetailUiState
    data object Error : DetailUiState
    data object Loading : DetailUiState
}
