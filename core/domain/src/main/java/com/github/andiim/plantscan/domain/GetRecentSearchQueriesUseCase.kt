package com.github.andiim.plantscan.domain

import com.github.andiim.plantscan.data.model.RecentSearchQuery
import com.github.andiim.plantscan.data.repository.RecentSearchRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/** A use case which returns the recent search queries. */
class GetRecentSearchQueriesUseCase
@Inject
constructor(private val recentSearchRepository: RecentSearchRepository) {
  operator fun invoke(limit: Int = 10): Flow<List<RecentSearchQuery>> =
      recentSearchRepository.getRecentSearchQueries(limit)
}
