package com.github.andiim.plantscan.app.ui.screens.detail

import com.github.andiim.plantscan.app.data.firebase.LogService
import com.github.andiim.plantscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject
constructor(logService: LogService) : PlantScanViewModel(logService) {

}