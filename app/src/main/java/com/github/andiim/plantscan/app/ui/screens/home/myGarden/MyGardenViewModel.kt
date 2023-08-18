package com.github.andiim.plantscan.app.ui.screens.home.myGarden

import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MyGardenViewModel @Inject constructor(useCase: PlantUseCase, logService: LogService) :
    PlantScanViewModel(logService) {
  val myPlant: StateFlow<List<Plant>> = useCase.getGarden()
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), listOf())
}
