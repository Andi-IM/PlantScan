package com.github.andiim.plantscan.app.data.firebase.implement

import androidx.paging.PagingData
import com.github.andiim.plantscan.app.data.firebase.PlantDatabase
import com.github.andiim.plantscan.app.data.model.Plant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FakePlantDatabaseImpl @Inject constructor() : PlantDatabase {
    override fun getAllPlant(): Flow<PagingData<Plant>> {
        TODO("Not yet implemented")
    }

    override fun getMyPlant(): Flow<PagingData<Plant>> {
        TODO("Not yet implemented")
    }

    override fun searchPlant(query: String): Flow<PagingData<Plant>> {
        TODO("Not yet implemented")
    }
}