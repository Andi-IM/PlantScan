package com.github.andiim.orchidscan.app.data.firebase

import androidx.paging.PagingData
import com.github.andiim.orchidscan.app.data.model.Plant
import kotlinx.coroutines.flow.Flow

interface PlantDatabase {
    fun getAllPlant(): Flow<PagingData<Plant>>
    fun getMyPlant(): Flow<PagingData<Plant>>
    fun searchPlant(query: String = ""): Flow<PagingData<Plant>>
}