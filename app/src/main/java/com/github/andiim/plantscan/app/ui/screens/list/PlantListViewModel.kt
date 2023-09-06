package com.github.andiim.plantscan.app.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import com.github.andiim.plantscan.app.core.data.PlantRepositoryImpl
import com.github.andiim.plantscan.app.core.domain.usecase.PlantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlantListViewModel
@Inject
constructor(private val useCase: PlantUseCase) : ViewModel() {
    val items =
        Pager(
            config = PlantRepositoryImpl.getDefaultPageConfig(),
            pagingSourceFactory = { useCase.getPlants() })
            .flow
}
