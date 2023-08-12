package com.github.andiim.plantscan.app.ui.screens.detail

import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class DetailViewModel
@Inject
constructor(private val useCase: PlantUseCase, logService: LogService) :
    PlantScanViewModel(logService) {
  private val _uiState: MutableStateFlow<Resource<Plant>> = MutableStateFlow(Resource.Empty)
  val uiState = _uiState.asStateFlow()

  private val _isSaved = MutableStateFlow(false)
  val isSaved = _isSaved.asStateFlow()

  fun retrieveSaveData(id: String?) {
    launchCatching {
      id?.let { useCase.isAddedToGarden(it).collect { data -> _isSaved.update { data } } }
    }
  }

  fun getDetail(id: String?) {
    if (id == null) _uiState.update { Resource.Error("ID can't be null!") }

    launchCatching {
      id?.let { useCase.getPlantDetail(it).collect { result -> _uiState.update { result } } }
    }
  }

  fun setPlantToGarden(plant: Plant) {
    launchCatching { useCase.addPlantToGarden(plant) }
    retrieveSaveData(plant.id)
  }

  fun removePlantFromGarden(plant: Plant) {
    launchCatching { useCase.removePlantFromGarden(plant) }
    retrieveSaveData(plant.id)
  }
}
