package com.github.andiim.plantscan.app.core.domain.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
  fun getAllPlant(): Flow<PagingData<Plant>>
  fun getPlantDetail(id: String): Flow<Resource<Plant>>
  fun searchPlant(query: String = ""): PagingSource<Int, Plant>
}
