package com.github.andiim.orchidscan.app.ui.screens.home.myGarden

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.github.andiim.orchidscan.app.data.firebase.LogService
import com.github.andiim.orchidscan.app.data.firebase.PlantDatabase
import com.github.andiim.orchidscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject
constructor(plantDatabase: PlantDatabase, logService: LogService) : PlantScanViewModel(logService) {
    val myPlant = plantDatabase.getMyPlant().cachedIn(viewModelScope)
}