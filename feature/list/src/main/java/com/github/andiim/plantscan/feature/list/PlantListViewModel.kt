package com.github.andiim.plantscan.feature.list

import androidx.lifecycle.SavedStateHandle
import androidx.paging.Pager
import com.github.andiim.plantscan.core.crashlytics.LogService
import com.github.andiim.plantscan.core.crashlytics.PlantScanViewModel
import com.github.andiim.plantscan.data.repository.PlantRepository
import com.github.andiim.plantscan.data.repository.PlantRepository.Companion.getDefaultPageConfig
import com.github.andiim.plantscan.data.repository.PlantResourceQuery
import com.github.andiim.plantscan.feature.list.navigation.TypeArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel
@Inject
constructor(
    private val repository: PlantRepository,
    logService: LogService,
    savedStateHandle: SavedStateHandle
) : PlantScanViewModel(logService) {

  private val typeArg: TypeArgs = TypeArgs(savedStateHandle)
  val list =
      Pager(
              config = getDefaultPageConfig(),
              pagingSourceFactory = { repository.getPlants(PlantResourceQuery(filterPlantType = null)) })
          .flow
}
