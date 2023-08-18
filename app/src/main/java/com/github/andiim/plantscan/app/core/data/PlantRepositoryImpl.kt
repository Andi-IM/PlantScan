package com.github.andiim.plantscan.app.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.mediator.PlantPagingSource
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.DbResponse
import com.github.andiim.plantscan.app.core.data.source.local.entity.PlantEntity
import com.github.andiim.plantscan.app.core.data.source.local.room.PlantDao
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.PlantDetail
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.RemotePlantSource
import com.github.andiim.plantscan.app.core.utils.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

@Singleton
class PlantRepositoryImpl
@Inject
constructor(
    private val remote: RemotePlantSource,
    private val local: PlantDao,
    private val executors: AppExecutors
) : PlantRepository {

  companion object {
    fun getDefaultPageConfig(): PagingConfig {
      return PagingConfig(
          pageSize = PlantPagingSource.NETWORK_PAGE_SIZE, enablePlaceholders = false)
    }
  }

  override fun getAllPlant(): Flow<PagingData<Plant>> =
      Pager(
              config = getDefaultPageConfig(),
              pagingSourceFactory = { PlantPagingSource(remote = remote) })
          .flow

  override fun getPlantDetail(id: String): Flow<Resource<Plant>> = flow {
    val response = remote.getPlantById(id)

    if (response is DbResponse.Success) {
      val plant = response.data.toDomain()

      val detailDocument = remote.getPlantDetail(id)
      if (detailDocument is DbResponse.Success) {
        val detail = detailDocument.data

        val genusResponse = remote.getGenus(detail.classification)
        if (genusResponse is DbResponse.Success) {
          val genus = genusResponse.data

          val phylumResponse = remote.getPhylum(genus.phylumRef)
          val classResponse = remote.getClass(genus.phylumRef, genus.classRef)
          val orderResponse = remote.getOrder(genus.orderRef)
          val familyResponse = remote.getFamily(genus.orderRef, genus.familyRef)

          if (phylumResponse is DbResponse.Success &&
              classResponse is DbResponse.Success &&
              orderResponse is DbResponse.Success &&
              familyResponse is DbResponse.Success) {

            val taxon =
                Taxonomy.mapFromResponse(
                    phylumResponse.data,
                    classResponse.data,
                    orderResponse.data,
                    familyResponse.data,
                    genus)

            val plantDetail = PlantDetail.mapFromResponse(detail, taxon)
            emit(Resource.Success(plant.copy(detail = plantDetail)))
          }
        }
      } else if (detailDocument is DbResponse.Error) {
        emit(Resource.Error(detailDocument.errorMessage))
      }
    }
  }

  override fun searchPlant(query: String): PagingSource<Int, Plant> =
      PlantPagingSource(remote = remote)

  override fun getGarden(): Flow<List<Plant>> = local.gardenList().map { Plant.mapFromEntity(it) }

  override fun isAddedToGarden(id: String): Flow<Boolean> = flow {
    val data = local.getPlantById(id)
    emit(data.first().isNotEmpty())
  }

  override fun addPlantToGarden(plant: Plant) {
    val entity = PlantEntity.mapFromDomain(plant)
    executors.diskIO().execute { local.addPlant(entity) }
  }

  override fun removePlantFromGarden(plant: Plant) {
    val entity = PlantEntity.mapFromDomain(plant)
    executors.diskIO().execute { local.removePlant(entity) }
  }
}
