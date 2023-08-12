package com.github.andiim.plantscan.app.core.data.source.firebase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FakePlantRepositoryImpl @Inject constructor() : PlantRepository {
  override fun getAllPlant(): Flow<PagingData<Plant>> {
    TODO("Not yet implemented")
  }

  override fun getPlantDetail(id: String): Flow<Resource<Plant>> {
    TODO("Not yet implemented")
  }

  override fun searchPlant(query: String): PagingSource<Int, Plant> {
    TODO("Not yet implemented")
  }

  override fun getGarden(): Flow<List<Plant>> {
    TODO("Not yet implemented")
  }

  override fun isAddedToGarden(id: String): Flow<Boolean> {
    TODO("Not yet implemented")
  }

  override fun addPlantToGarden(plant: Plant) {
    TODO("Not yet implemented")
  }

  override fun removePlantFromGarden(plant: Plant) {
    TODO("Not yet implemented")
  }

}
