package com.github.andiim.plantscan.app.core.data.firebase.implement

import androidx.paging.PagingData
import com.github.andiim.orchidscan.app.data.firebase.PlantDatabase
import com.github.andiim.orchidscan.app.data.model.Plant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PlantRepositoryImpl @Inject constructor() : PlantDatabase {
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