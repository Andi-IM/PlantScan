package com.github.andiim.plantscant.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.github.andiim.plantscant.core.database.model.RecentSearchQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchQueryDao {
  @Query("SELECT * FROM recentSearchQueries ORDER BY queriedDate DESC LIMIT :limit")
  fun getRecentSearchQueryEntities(limit: Int): Flow<List<RecentSearchQueryEntity>>

  @Upsert
  suspend fun insertOrReplaceRecentSearchQuery(recentSearchQueryEntity: RecentSearchQueryEntity)

  @Query("DELETE FROM recentSearchQueries")
  suspend fun clearRecentSearchQueries()
}
