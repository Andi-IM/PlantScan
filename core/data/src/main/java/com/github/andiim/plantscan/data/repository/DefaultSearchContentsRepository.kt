package com.github.andiim.plantscan.data.repository

import com.github.andiim.plantscan.core.common.network.Dispatcher
import com.github.andiim.plantscan.core.common.network.PlantScantDispatchers.IO
import com.github.andiim.plantscan.model.data.SearchResult
import com.github.andiim.plantscant.core.database.dao.PlantDao
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class DefaultSearchContentsRepository
@Inject
constructor(
    val plantDao: PlantDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : SearchContentsRepository {
  override suspend fun populateFtsData() {
    TODO("Not yet implemented")
  }

  override fun searchContents(searchQuery: String): Flow<SearchResult> {
    TODO("Not yet implemented")
  }

  override fun getSearchContentsCount(): Flow<Int> {
    TODO("Not yet implemented")
  }
}
