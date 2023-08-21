package com.github.andiim.plantscan.domain

import android.net.Uri
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.core.common.result.Resource
import com.github.andiim.plantscan.model.data.Plant
import com.github.andiim.plantscan.model.data.PlantType
import java.io.File
import kotlinx.coroutines.flow.Flow

interface PlantUseCase {
  fun getPlantTypes(): PagingSource<Int, PlantType>
  fun getPlantsByType(type: String = ""): PagingSource<Int, Plant>
  fun getAllPlant(): Flow<PagingData<Plant>>
  fun getPlantDetail(id: String): Flow<Resource<Plant>>
  fun searchPlant(query: String = ""): PagingSource<Int, Plant>
  fun getGarden(): Flow<List<Plant>>
  fun isAddedToGarden(id: String): Flow<Boolean>
  fun addPlantToGarden(plant: Plant)
  fun removePlantFromGarden(plant: Plant)


  fun notifyImageCreated(savedUri: Uri)
  fun createImageOutputFile(): File
}
