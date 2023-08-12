package com.github.andiim.plantscan.app.ui.screens.list

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.andiim.plantscan.app.core.data.source.firebase.LogService
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    plantRepository: PlantRepository,
    logService: LogService
) : PlantScanViewModel(logService) {
    val fetchedData = plantRepository.getAllPlant().cachedIn(viewModelScope)
}