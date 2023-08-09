package com.github.andiim.plantscan.app.ui.screens.list

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.andiim.plantscan.app.data.firebase.LogService
import com.github.andiim.plantscan.app.data.firebase.PlantDatabase
import com.github.andiim.plantscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel @Inject constructor(
    plantDatabase: PlantDatabase,
    logService: LogService
) : PlantScanViewModel(logService) {
    val fetchedData = plantDatabase.getAllPlant().cachedIn(viewModelScope)
}