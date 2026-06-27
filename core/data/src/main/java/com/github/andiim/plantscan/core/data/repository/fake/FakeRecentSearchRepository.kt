package com.github.andiim.plantscan.core.data.repository.fake

import com.github.andiim.plantscan.core.data.model.RecentSearchQuery
import com.github.andiim.plantscan.core.data.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Fake implementation of the [RecentSearchRepository].
 */
class FakeRecentSearchRepository @Inject constructor() : RecentSearchRepository {
    override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
        flowOf(emptyList())

    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) = Unit

    override suspend fun clearRecentSearches() = Unit
}
