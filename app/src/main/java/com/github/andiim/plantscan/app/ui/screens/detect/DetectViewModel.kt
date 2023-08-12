package com.github.andiim.plantscan.app.ui.screens.detect

import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import com.github.andiim.plantscan.app.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetectViewModel @Inject constructor(logService: LogService) :
    PlantScanViewModel(logService) {

}