package com.github.andiim.plantscan.app.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase.Companion.getDefaultPageConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel
@Inject
constructor(private val useCase: PlantUseCase) : ViewModel() {
    val items: Flow<PagingData<Plant>> =
        Pager(
            config = getDefaultPageConfig(),
            pagingSourceFactory = { useCase.getPlants() }
        )
            .flow
}
