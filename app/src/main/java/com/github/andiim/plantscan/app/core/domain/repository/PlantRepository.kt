package com.github.andiim.plantscan.app.core.domain.repository

import androidx.paging.PagingSource
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Plant
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import kotlinx.coroutines.flow.Flow

interface PlantRepository {
    fun getPlants(query: String): PagingSource<Int, Plant>
    fun getPlantDetail(id: String): Flow<Resource<Plant>>
    fun getPlantBySpecies(species: String): Flow<Resource<Plant>>
    fun detect(base64ImageData: String, confidence: Int): Flow<Resource<ObjectDetection>>
    fun recordDetection(detection: DetectionHistory): Flow<String>
    fun getDetectionsList(userId: String): Flow<Resource<List<DetectionHistory>>>
    fun sendSuggestion(suggestion: Suggestion): Flow<String>
}
