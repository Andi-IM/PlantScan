package com.github.andiim.plantscan.core.data.repository.fake

import com.github.andiim.plantscan.core.data.repository.SearchContentsRepository
import com.github.andiim.plantscan.core.model.data.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Fake implementation of the [SearchContentsRepository].
 */
class FakeSearchContentsRepository @Inject constructor() : SearchContentsRepository {
    // suspend fun populateFtsData() = Unit
    override fun searchContents(searchQuery: String): Flow<SearchResult> = flowOf()
    override fun getSearchContentsCount(): Flow<Int> = flowOf(1)
}
