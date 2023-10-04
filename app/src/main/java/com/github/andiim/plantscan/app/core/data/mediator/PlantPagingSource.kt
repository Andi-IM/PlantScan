package com.github.andiim.plantscan.app.core.data.mediator

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.firestore.FirestoreSource
import com.github.andiim.plantscan.app.core.firestore.model.toModel

class PlantPagingSource(
    private val remote: FirestoreSource,
) : PagingSource<Int, Plant>() {
    private var query: String = ""

    fun setQuery(value: String) {
        query = value
    }

    override fun getRefreshKey(state: PagingState<Int, Plant>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Plant> {
        val position = params.key ?: STARTING_PAGE_INDEX
        val response = remote.getPlants(NETWORK_PAGE_SIZE.toLong(), query)

        val endPaginationReached = if (position < NETWORK_PAGE_SIZE) true else response.isEmpty()
        val results = response.map { it.toModel() }

        return LoadResult.Page(
            data = results,
            prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
            nextKey = if (endPaginationReached) null else position + 1
        )
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 5
    }
}
