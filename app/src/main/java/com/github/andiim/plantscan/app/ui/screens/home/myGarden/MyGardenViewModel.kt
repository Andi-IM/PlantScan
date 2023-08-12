package com.github.andiim.plantscan.app.ui.screens.home.myGarden

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.andiim.plantscan.app.core.data.source.firebase.LogService
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject
constructor(plantRepository: PlantRepository, logService: LogService) : PlantScanViewModel(logService) {
    val myPlant = plantRepository.getMyPlant().cachedIn(viewModelScope)
}