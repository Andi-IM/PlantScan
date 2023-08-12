package com.github.andiim.plantscan.app.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PlantInteractor @Inject constructor(private val repo: PlantRepository) : PlantUseCase {
  override fun getAllPlant(): Flow<PagingData<Plant>> = repo.getAllPlant()
  override fun getPlantDetail(id: String): Flow<Resource<Plant>> = repo.getPlantDetail(id)

  override fun searchPlant(query: String): PagingSource<Int, Plant> = repo.searchPlant(query)

  override fun getGarden(): Flow<List<Plant>> = repo.getGarden()

  override fun isAddedToGarden(id: String): Flow<Boolean> = repo.isAddedToGarden(id)

  override fun addPlantToGarden(plant: Plant) = repo.addPlantToGarden(plant)

  override fun removePlantFromGarden(plant: Plant) = repo.removePlantFromGarden(plant)
}
