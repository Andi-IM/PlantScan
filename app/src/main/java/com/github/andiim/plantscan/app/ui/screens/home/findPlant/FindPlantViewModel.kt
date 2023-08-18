package com.github.andiim.plantscan.app.ui.screens.home.findPlant

import androidx.paging.Pager
import com.github.andiim.plantscan.app.PlantScanViewModel
import com.github.andiim.plantscan.app.core.data.PlantRepositoryImpl.Companion.getDefaultPageConfig
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@HiltViewModel
class FindPlantViewModel
@Inject
constructor(private val useCase: PlantUseCase, logService: LogService) :
    PlantScanViewModel(logService) {

  private val queryState = MutableStateFlow("")
  val query: StateFlow<String> = queryState.asStateFlow()

  val items =
      Pager(
              config = getDefaultPageConfig(),
              pagingSourceFactory = { useCase.searchPlant(queryState.value) })
          .flow

  fun onQueryChange(queryData: String) {
    launchCatching { queryState.debounce(1.seconds).collect { queryState.value = queryData } }
  }

  fun onSearch(query: String) {
    launchCatching { queryState.value = query }
  }
}
