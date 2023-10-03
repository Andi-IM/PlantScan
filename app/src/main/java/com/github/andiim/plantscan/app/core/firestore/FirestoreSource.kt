package com.github.andiim.plantscan.app.core.firestore

import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.ImageContent
import com.github.andiim.plantscan.app.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.app.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.flow.Flow

interface FirestoreSource {
    suspend fun getPlants(
        limit: Long,
        query: String = "",
    ): List<PlantDocument>

    suspend fun getPlantById(id: String): PlantDocument
    suspend fun getPlantBySpecies(species: String): PlantDocument
    suspend fun recordDetection(detection: DetectionHistoryDocument): String
    suspend fun getDetectionsList(userId: String): List<DetectionHistoryDocument>
    suspend fun sendASuggestion(suggestion: SuggestionDocument): String
    fun uploadSuggestionImage(content: ImageContent): Flow<String>
}
