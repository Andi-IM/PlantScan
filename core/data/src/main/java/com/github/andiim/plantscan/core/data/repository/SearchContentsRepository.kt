package com.github.andiim.plantscan.core.data.repository

import com.github.andiim.plantscan.core.model.data.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Data layer interface for the search feature.
 */
interface SearchContentsRepository {
    /**
     * Populate the fts tables for the search contents.
     */

    /**
     * Query the contents matched the [searchQuery] and returns it as a [Flow] of [SearchResult].
     */
    fun searchContents(searchQuery: String): Flow<SearchResult>

    fun getSearchContentsCount(): Flow<Int>
}

class DefaultSearchContentsRepository @Inject constructor(
    private val plantRepository: PlantRepository,
    // private val plantDao: PlantDao,
    // private val plantFtsDao: PlantFtsDao,
) : SearchContentsRepository {

    override fun searchContents(searchQuery: String): Flow<SearchResult> =
        plantRepository.searchPlants(searchQuery).map(::SearchResult)
    /*{
        val plantIds = plantFtsDao.searchAllPlants("*$searchQuery")
        val plantsFlow = plantIds
            .mapLatest { it.toSet() }
            .distinctUntilChanged()
            .flatMapLatest(plantDao::getPlantEntities)
        return plantsFlow.map { plants ->
            SearchResult(plants = plants.map(PlantAndImages::asExternalModel))
        }
    }*/

    override fun getSearchContentsCount(): Flow<Int> =
        plantRepository.countPlant()
}
