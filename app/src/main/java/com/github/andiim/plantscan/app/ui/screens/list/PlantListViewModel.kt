package com.github.andiim.plantscan.app.ui.screens.list

import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel
@Inject
constructor(private val useCase: PlantUseCase, logService: LogService) :
    PlantScanViewModel(logService) {
  fun fetchData() = useCase.getAllPlant()
}
