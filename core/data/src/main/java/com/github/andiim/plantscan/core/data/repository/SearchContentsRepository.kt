package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.database.dao.PlantDao
import com.github.andiim.plantscan.core.database.dao.PlantFtsDao
import com.github.andiim.plantscan.core.database.model.PlantAndImages
import com.github.andiim.plantscan.core.database.model.asExternalModel
import com.github.andiim.plantscan.core.database.model.asFtsEntity
import com.github.andiim.plantscan.core.model.data.Plant
import com.github.andiim.plantscan.core.model.data.SearchResult
import com.github.andiim.plantscan.core.network.AppDispatchers.IO
import com.github.andiim.plantscan.core.network.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Data layer interface for the search feature.
 */
interface SearchContentsRepository {
    /**
     * Populate the fts tables for the search contents.
     */
    suspend fun populateFtsData()

    /**
     * Query the contents matched the [searchQuery] and returns it as a [Flow] of [SearchResult].
     */
    fun searchContents(searchQuery: String): Flow<SearchResult>

    fun getSearchContentsCount(): Flow<Int>
}

class DefaultSearchContentsRepository @Inject constructor(
    private val plantRepository: PlantRepository,
    private val plantDao: PlantDao,
    private val plantFtsDao: PlantFtsDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : SearchContentsRepository {
    override suspend fun populateFtsData() {
        withContext(ioDispatcher) {
            val plants = plantRepository.getPlants().map { it.map(Plant::asFtsEntity) }.first()
            plantFtsDao.insertAll(plants)
        }
    }

    override fun searchContents(searchQuery: String): Flow<SearchResult> {
        val plantIds = plantFtsDao.searchAllPlants("*$searchQuery")
        val plantsFlow = plantIds
            .mapLatest { it.toSet() }
            .distinctUntilChanged()
            .flatMapLatest(plantDao::getPlantEntities)
        return plantsFlow.map { plants ->
            SearchResult(plants = plants.map(PlantAndImages::asExternalModel))
        }
    }

    override fun getSearchContentsCount(): Flow<Int> =
        plantFtsDao.getCount()
}
