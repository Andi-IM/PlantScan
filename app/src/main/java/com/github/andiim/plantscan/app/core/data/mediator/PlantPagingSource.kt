package com.github.andiim.plantscan.app.core.data.mediator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.DbResponse
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.RemotePlantSource

class PlantPagingSource(private val remote: RemotePlantSource, private val query: String = "") :
    PagingSource<Int, Plant>() {

  override fun getRefreshKey(state: PagingState<Int, Plant>): Int? = state.anchorPosition

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Plant> {
    val position = params.key ?: STARTING_PAGE_INDEX
    return when (val response =
        if (query.isEmpty()) remote.getAllPlant() else remote.searchPlant(query)) {
      is DbResponse.Success -> {
        val endPaginationReached = response.data.isEmpty()
        val results = response.data.map { it.toDomain() }

        LoadResult.Page(
            data = results,
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

  companion object {
    private const val STARTING_PAGE_INDEX = 1
    const val NETWORK_PAGE_SIZE = 5
  }
}
