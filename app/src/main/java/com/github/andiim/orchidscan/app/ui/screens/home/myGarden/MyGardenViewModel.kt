package com.github.andiim.orchidscan.app.ui.screens.home.myGarden

import com.github.andiim.orchidscan.app.data.firebase.LogService
import com.github.andiim.orchidscan.app.ui.screens.viewModels.PlantScanViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyGardenViewModel @Inject
constructor(logService: LogService) : PlantScanViewModel(logService) {
}