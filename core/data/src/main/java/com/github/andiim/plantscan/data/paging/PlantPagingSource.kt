package com.github.andiim.plantscan.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.andiim.plantscan.core.firestore.firestore.FirestoreCollections
import com.github.andiim.plantscan.data.paging.Constants.STARTING_PAGE_INDEX
import com.github.andiim.plantscan.data.repository.PlantResourceQuery
import com.github.andiim.plantscan.model.data.Plant
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PlantPagingSource(
    private val remote: FirestoreCollections,
    private val query: PlantResourceQuery,
) : PagingSource<Int, Plant>() {

  override fun getRefreshKey(state: PagingState<Int, Plant>): Int? = state.anchorPosition

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Plant> {
    val position = params.key ?: STARTING_PAGE_INDEX

    try {
      val results =
          remote
              .getPlants(query = query.toPlantQuery())
              .map { list -> list.map { it.toDomain() } }
              .first()

      val endPaginationReached = results.isEmpty()

      return LoadResult.Page(
          data = results,
          prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
          nextKey = if (endPaginationReached) null else position + 1)
    } catch (e: Exception) {
      return LoadResult.Error(e)
    }
  }
}
