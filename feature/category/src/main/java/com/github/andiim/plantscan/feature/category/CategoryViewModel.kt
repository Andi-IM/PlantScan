package com.github.andiim.plantscan.feature.category

import androidx.paging.Pager
import com.github.andiim.plantscan.core.crashlytics.LogService
import com.github.andiim.plantscan.core.crashlytics.PlantScanViewModel
import com.github.andiim.plantscan.data.repository.PlantRepository.Companion.getDefaultPageConfig
import com.github.andiim.plantscan.domain.PlantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel
@Inject
constructor(
    useCase: PlantUseCase,
    logService: LogService
) : PlantScanViewModel(logService) {

  val types =
      Pager(config = getDefaultPageConfig(), pagingSourceFactory = { useCase.getPlantTypes() }).flow
}
