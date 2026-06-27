package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.data.model.RecentSearchQuery
import com.github.andiim.plantscan.core.data.model.asExternalModel
import com.github.andiim.plantscan.core.database.dao.RecentSearchQueryDao
import com.github.andiim.plantscan.core.database.model.RecentSearchQueryEntity
import com.github.andiim.plantscan.core.network.AppDispatchers.IO
import com.github.andiim.plantscan.core.network.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import javax.inject.Inject

/**
 * Data layer interface for the recent searches.
 */
interface RecentSearchRepository {
    /**
     * Get the recent search queries up to the number of queries specified as [limit].
     */
    fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>>

    /**
     * Insert or replace the [searchQuery] as part of the recent search.
     */
    suspend fun insertOrReplaceRecentSearch(searchQuery: String)

    /**
     * Clear the recent searches.
     */
    suspend fun clearRecentSearches()
}

class DefaultRecentSearchRepository @Inject constructor(
    private val recentSearchQueryDao: RecentSearchQueryDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : RecentSearchRepository {
    override fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>> =
        recentSearchQueryDao.getRecentSearchQueryEntities(limit).map { searchQueries ->
            searchQueries.map {
                it.asExternalModel()
            }
        }

    override suspend fun insertOrReplaceRecentSearch(searchQuery: String) {
        withContext(ioDispatcher) {
            recentSearchQueryDao.insertOrReplaceRecentSearchQuery(
                RecentSearchQueryEntity(
                    query = searchQuery,
                    queriedDate = Clock.System.now()
                )
            )
        }
    }

    override suspend fun clearRecentSearches() = recentSearchQueryDao.clearRecentSearchQueries()
}
