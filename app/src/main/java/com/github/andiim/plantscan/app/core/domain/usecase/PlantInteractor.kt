package com.github.andiim.plantscan.app.core.domain.usecase

import android.net.Uri
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
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
    override fun getPlants(query: String): PagingSource<Int, Plant> = plantRepo.getPlants(query)

    override fun getPlantDetail(id: String): Flow<Resource<Plant>> = plantRepo.getPlantDetail(id)
    override fun detect(image: File): Flow<Resource<ObjectDetection>> = plantRepo.detect(image)
    override fun recordDetection(detection: DetectionHistory): Flow<String> =
        plantRepo.recordDetection(detection)

    override fun getDetectionsList(): Flow<Resource<List<DetectionHistory>>> =
        plantRepo.getDetectionsList()

    override fun notifyImageCreated(savedUri: Uri) = cameraRepo.notifyImageCreated(savedUri)
    override fun createImageOutputFile(): File = cameraRepo.createImageOutputFile()
}
