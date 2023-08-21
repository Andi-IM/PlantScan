package com.github.andiim.plantscan.testing.repository

import androidx.paging.PagingSource
import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.data.repository.PlantRepository
import com.github.andiim.plantscan.data.repository.PlantResourceQuery
import com.github.andiim.plantscan.model.data.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestPlantRepository : PlantRepository {
    /**
     * The backing hot flow for the list of plants ids for testing.
     */

    private val plantResourcesFlow: MutableSharedFlow<List<Plant>> = MutableSharedFlow()
    override fun getPlants(query: PlantResourceQuery): PagingSource<Int, Plant> {
        TODO("Not yet implemented")
    }

    override fun getPlantDetail(id: String): Flow<Resource<Plant>> {
        TODO("Not yet implemented")
    }
}