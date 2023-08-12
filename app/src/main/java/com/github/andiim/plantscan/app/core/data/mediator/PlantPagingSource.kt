package com.github.andiim.plantscan.app.core.data.mediator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.andiim.plantscan.app.core.data.source.firebase.RemotePlantSource
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.DbResponse
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.PlantDetail
import com.github.andiim.plantscan.app.core.domain.model.Taxonomy

class PlantPagingSource(private val remote: RemotePlantSource, private val query: String = "") :
    PagingSource<Int, Plant>() {

  override fun getRefreshKey(state: PagingState<Int, Plant>): Int? = state.anchorPosition

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Plant> {
    val position = params.key ?: STARTING_PAGE_INDEX
    return when (val response =
        if (query.isEmpty()) remote.getAllPlant() else remote.searchPlant(query)) {
      is DbResponse.Success -> {
        val endPaginationReached = response.data.isEmpty()
        val results = Plant.mapFromResponse(response.data)

        LoadResult.Page(
            data = fetchPlants(results),
            prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
            nextKey = if (endPaginationReached) null else position + 1)
      }
      is DbResponse.Empty -> {
        LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
      }
      is DbResponse.Error -> {
        LoadResult.Error(Exception(response.errorMessage))
      }
    }
  }

  private suspend fun fetchPlants(plants: List<Plant>): List<Plant> =
      plants.map { plant ->
        val detailDocument = remote.getPlantDetail(plant.id)
        if (detailDocument is DbResponse.Success) {
          val response = detailDocument.data

          val genusResponse = remote.getGenus(response.classification)
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

              val plantDetail = PlantDetail.mapFromResponse(response, taxon)
              plant.detail = plantDetail
            }
          }
        }
        plant
      }

  companion object {
    private const val STARTING_PAGE_INDEX = 1
    const val NETWORK_PAGE_SIZE = 5
  }
}
