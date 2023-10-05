package com.github.andiim.plantscan.app.ui.screens.home.findPlant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.firebaseServices.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FindPlantViewModel
@Inject
constructor(
    private val useCase: PlantUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val logService: LogService,
) : ViewModel() {

    val query = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    val items = Pager(PlantUseCase.getDefaultPageConfig()) {
        search(query.value)
    }
        .flow.cachedIn(viewModelScope)

    fun onQueryChange(queryData: String) {
        savedStateHandle[SEARCH_QUERY] = queryData
    }

    fun onSearch(query: String) {
        // launchCatching(logService) { queryState.value = query }
    }

    private fun search(queryData: String): PagingSource<Int, Plant> {
        return useCase.getPlants(queryData)
    }
}


private const val SEARCH_QUERY = "searchQuery"