package com.github.andiim.plantscan.feature.mygarden

import androidx.lifecycle.viewModelScope
import com.github.andiim.plantscan.core.common.base.PlantScanViewModel
import com.github.andiim.plantscan.domain.PlantUseCase
import com.github.andiim.plantscan.domain.firebase_services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MyGardenViewModel @Inject constructor(useCase: com.github.andiim.plantscan.domain.PlantUseCase, logService: com.github.andiim.plantscan.domain.firebase_services.LogService) :
    com.github.andiim.plantscan.core.common.base.PlantScanViewModel(logService) {
  val myPlant: StateFlow<List<com.github.andiim.plantscan.model.data.Plant>> = useCase.getGarden()
          .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), listOf())
}
