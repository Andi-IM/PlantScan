package com.github.andiim.plantscan.app.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import kotlinx.coroutines.flow.Flow

interface PlantUseCase {
  fun getAllPlant(): Flow<PagingData<Plant>>
  fun getPlantDetail(id: String): Flow<Resource<Plant>>
  fun searchPlant(query: String = ""): PagingSource<Int, Plant>
  fun getGarden(): Flow<List<Plant>>
  fun isAddedToGarden(id: String): Flow<Boolean>
  fun addPlantToGarden(plant: Plant)
  fun removePlantFromGarden(plant: Plant)
}
