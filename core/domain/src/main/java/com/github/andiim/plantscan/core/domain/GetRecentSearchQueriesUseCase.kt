package com.github.andiim.plantscan.core.domain

import com.github.andiim.plantscan.core.data.model.RecentSearchQuery
import com.github.andiim.plantscan.core.data.repository.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A use case which returns the recent search queries.
 */
class GetRecentSearchQueriesUseCase @Inject constructor(
    private val recentSearchRepository: RecentSearchRepository,
) {
    operator fun invoke(limit: Int = 10): Flow<List<RecentSearchQuery>> =
        recentSearchRepository.getRecentSearchQueries(limit)
}