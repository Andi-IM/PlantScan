package com.github.andiim.plantscan.app.core.data.source.firebase.implement

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import com.github.andiim.plantscan.app.core.domain.model.Plant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FakePlantRepositoryImpl @Inject constructor() : PlantRepository {

    override fun getAllPlant(pagingConfig: PagingConfig): Flow<PagingData<Plant>> {
        TODO("Not yet implemented")
    }

    override fun getMyPlant(pagingConfig: PagingConfig): Flow<PagingData<Plant>> {
        TODO("Not yet implemented")
    }

    override fun searchPlant(query: String, pagingConfig: PagingConfig): Flow<PagingData<Plant>> {
        TODO("Not yet implemented")
    }
}