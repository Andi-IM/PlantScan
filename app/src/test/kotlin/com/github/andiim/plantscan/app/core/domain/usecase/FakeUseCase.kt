package com.github.andiim.plantscan.app.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.utils.DataDummy
import com.github.andiim.plantscan.app.core.domain.model.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeUseCase : PlantUseCase {
  val data = mutableListOf<Plant>()

  fun addPlant(plant: Plant) {
    data.add(plant)
  }

  private fun addAllPlant(plants: List<Plant>) {
    data.addAll(plants)
  }

  private val pagingSource = data.asPagingSourceFactory()
  override fun getAllPlant(): Flow<PagingData<Plant>> = flow { emit(PagingData.from(data)) }

  override fun getPlantDetail(id: String): Flow<Resource<Plant>> {
    TODO("Not yet implemented")
  }

  override fun searchPlant(query: String): PagingSource<Int, Plant> {
    if (query.isEmpty()) {
      return pagingSource()
    }
    addAllPlant(DataDummy.PLANTS.filter { plant -> plant.name == query || plant.species == query })
    return pagingSource()
  }

  override fun getGarden(): Flow<List<Plant>> {
    addAllPlant(DataDummy.PLANTS)
    return flowOf(data)
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
