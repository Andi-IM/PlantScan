package com.github.andiim.plantscan.app.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.utils.DataDummy
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

@Suppress("SpellCheckingInspection")
class FakePlantInteractor @Inject constructor() : PlantUseCase {
  private val data = mutableListOf<Plant>()

  private fun addPlant(plant: Plant) {
    data.add(plant)
  }

  private fun removePlant(plant: Plant) {
    data.removeIf { it == plant }
  }

  private fun addAllPlant(plants: List<Plant>) {
    data.addAll(plants)
  }

  private val pagingSource = data.asPagingSourceFactory()
  override fun getAllPlant(): Flow<PagingData<Plant>> = flow { emit(PagingData.from(data)) }

  override fun getPlantDetail(id: String): Flow<Resource<Plant>> {
    if (id.isEmpty()) {
      return flowOf(Resource.Empty)
    }
    DataDummy.PLANTS.find { plant -> plant.id == id }?.let { addPlant(it) }
    return flowOf(Resource.Success(data.first()))
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
    if (id.isEmpty()) {
      return flowOf(false)
    }
    val expected = data.find { plant -> plant.id == id }
    return flowOf(expected == null)
  }

  override fun addPlantToGarden(plant: Plant) = addPlant(plant)

  override fun removePlantFromGarden(plant: Plant) = removePlant(plant)
}
