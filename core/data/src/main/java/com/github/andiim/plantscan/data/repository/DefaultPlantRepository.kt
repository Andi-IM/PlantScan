package com.github.andiim.plantscan.data.repository

import androidx.paging.PagingSource
import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.core.firestore.firestore.FirestoreCollections
import com.github.andiim.plantscan.data.paging.PlantPagingSource
import com.github.andiim.plantscan.model.data.Plant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class DefaultPlantRepository
@Inject
constructor(
    private val collection: FirestoreCollections,
) : PlantRepository {
  override fun getPlants(query: PlantResourceQuery): PagingSource<Int, Plant> {
    return PlantPagingSource(collection, query)
  }
  override fun getPlantDetail(id: String): Flow<Resource<Plant>> = flow {
    try {
      val data = collection.getDetailFromId(id).first()
      emit(Resource.Success(data.toDomain()))
    } catch (e: Exception) {
      emit(Resource.Error(e))
    }
  }
}
