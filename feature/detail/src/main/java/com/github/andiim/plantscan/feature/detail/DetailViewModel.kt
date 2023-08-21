package com.github.andiim.plantscan.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.crashlytics.LogService
import com.github.andiim.plantscan.core.crashlytics.PlantScanViewModel
import com.github.andiim.plantscan.data.repository.MyGardenRepository
import com.github.andiim.plantscan.data.repository.PlantRepository
import com.github.andiim.plantscan.feature.detail.navigation.PlantArgs
import com.github.andiim.plantscan.model.data.Plant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DetailViewModel
@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val plantRepository: PlantRepository,
    private val myGardenRepository: MyGardenRepository,
    logService: LogService,
) : PlantScanViewModel(logService) {
  private val plantIdArg: PlantArgs = PlantArgs(savedStateHandle)
  val plantId = plantIdArg.plantId

  private val _savedStatus: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val savedStatus = _savedStatus.asStateFlow()

  val detailUiState: StateFlow<DetailUiState> =
      detailUiState(
              plantId = plantId,
              plantRepository = plantRepository,
          )
          .stateIn(
              scope = viewModelScope,
              started = SharingStarted.WhileSubscribed(5_000),
              initialValue = DetailUiState.Loading)

  private fun detailUiState(
      plantId: String,
      plantRepository: PlantRepository
  ): Flow<DetailUiState> {
    // Observe the followed topics, as they could change over time.
    val detail: Flow<Resource<Plant>> = plantRepository.getPlantDetail(id = plantId)
    return detail.map { result ->
      when (result) {
        is Resource.Success -> {
          val data = result.data
          DetailUiState.Success(data)
        }
        is Resource.Loading -> DetailUiState.Loading
        is Resource.Error -> DetailUiState.Error
      }
    }
  }

  fun retrieveSaveData() {
    launchCatching {
      myGardenRepository.plants.collect {
        it.find { data -> data.id == plantId }.let { exist -> _savedStatus.value = exist == null }
      }
    }
  }

  fun setPlantToGarden(plant: Plant) {
    retrieveSaveData()
    launchCatching {
      if (_savedStatus.value) myGardenRepository.removePlantFromGarden(plant)
      else myGardenRepository.addPlantToGarden(plant)
    }
  }
}

sealed interface DetailUiState {
  data class Success(val plantDetail: Plant) : DetailUiState
  data object Error : DetailUiState
  data object Loading : DetailUiState
}
