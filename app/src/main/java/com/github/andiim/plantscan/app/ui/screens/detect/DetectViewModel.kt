package com.github.andiim.plantscan.app.ui.screens.detect

import com.github.andiim.plantscan.app.core.data.source.firebase.LogService
import com.github.andiim.plantscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetectViewModel @Inject constructor(logService: LogService) :
    PlantScanViewModel(logService) {

}