package com.github.andiim.plantscan.app.ui.screens.home.findPlant

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebaseServices.LogService
import com.github.andiim.plantscan.app.ui.common.extensions.launchCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
@HiltViewModel
class FindPlantViewModel
@Inject
constructor(
    private val useCase: PlantUseCase,
    private val logService: LogService,
) : ViewModel() {

    private val queryState = MutableStateFlow("")
    val query: StateFlow<String> = queryState.asStateFlow()

    val items = Pager(
        config = PlantUseCase.getDefaultPageConfig(),
        pagingSourceFactory = { useCase.getPlants(queryState.value) }
    )
        .flow

    fun onQueryChange(queryData: String) {
        launchCatching(logService) {
            queryState.debounce(1.seconds).collect { queryState.value = queryData }
        }
    }

    fun onSearch(query: String) {
        launchCatching(logService) { queryState.value = query }
    }
}
