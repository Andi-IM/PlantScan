package com.github.andiim.plantscan.data.repository

import com.github.andiim.plantscan.core.common.network.Dispatcher
import com.github.andiim.plantscan.core.common.network.PlantScantDispatchers.IO
import com.github.andiim.plantscan.data.model.RecentSearchQuery
import com.github.andiim.plantscan.data.model.asExternalModel
import com.github.andiim.plantscant.core.database.dao.RecentSearchQueryDao
import com.github.andiim.plantscant.core.database.model.RecentSearchQueryEntity
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class DefaultRecentSearchRepository
@Inject
constructor(
    private val recentSearchQueryDao: RecentSearchQueryDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : RecentSearchRepository {
  override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
      recentSearchQueryDao.getRecentSearchQueryEntities(limit).map { searchQueries ->
        searchQueries.map { it.asExternalModel() }
      }

  override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
    withContext(ioDispatcher) {
      recentSearchQueryDao.insertOrReplaceRecentSearchQuery(
          RecentSearchQueryEntity(
              query = searchQuery,
              queriedDate = Clock.System.now(),
          ),
      )
    }
  }

  override suspend fun clearRecentSearches() = recentSearchQueryDao.clearRecentSearchQueries()
}
