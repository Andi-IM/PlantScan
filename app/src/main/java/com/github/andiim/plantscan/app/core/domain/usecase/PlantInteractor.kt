package com.github.andiim.plantscan.app.core.domain.usecase

import android.net.Uri
import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.github.andiim.plantscan.app.core.domain.repository.CameraRepository
import com.github.andiim.plantscan.app.core.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class PlantInteractor
@Inject
constructor(
    private val plantRepo: PlantRepository,
    private val cameraRepo: CameraRepository,
) :
    PlantUseCase {
    override fun getPlants(query: String): PagingSource<Int, Plant> = plantRepo.getPlants(query)
    override fun getPlantDetail(id: String): Flow<Resource<Plant>> = plantRepo.getPlantDetail(id)
    override fun getPlantBySpecies(species: String): Flow<Resource<Plant>> =
        plantRepo.getPlantBySpecies(species)

    override fun detect(base64ImageData: String, confidence: Int): Flow<Resource<ObjectDetection>> =
        plantRepo.detect(base64ImageData, confidence)

    override fun recordDetection(detection: DetectionHistory): Flow<String> =
        plantRepo.recordDetection(detection)

    override fun getDetectionsList(userId: String): Flow<Resource<List<DetectionHistory>>> =
        plantRepo.getDetectionsList(userId)

    override fun sendSuggestion(suggestion: Suggestion): Flow<String> =
        plantRepo.sendSuggestion(suggestion)

    override fun notifyImageCreated(savedUri: Uri) = cameraRepo.notifyImageCreated(savedUri)
    override fun createImageOutputFile(): File = cameraRepo.createImageOutputFile()
}
