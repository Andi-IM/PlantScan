package com.github.andiim.plantscan.app.core.domain.usecase

import android.net.Uri
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.repository.CameraRepository
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PlantInteractor
@Inject
constructor(private val plantRepo: PlantRepository, private val cameraRepo: CameraRepository) :
    PlantUseCase {
    override fun getAllPlant(): Flow<PagingData<Plant>> = plantRepo.getAllPlant()
    override fun getPlantDetail(id: String): Flow<Resource<Plant>> = plantRepo.getPlantDetail(id)
    override fun searchPlant(query: String): PagingSource<Int, Plant> = plantRepo.searchPlant(query)
    override fun notifyImageCreated(savedUri: Uri) = cameraRepo.notifyImageCreated(savedUri)
    override fun createImageOutputFile(): File = cameraRepo.createImageOutputFile()
}
