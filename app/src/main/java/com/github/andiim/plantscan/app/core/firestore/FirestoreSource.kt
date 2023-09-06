package com.github.andiim.plantscan.app.core.firestore

import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.PlantResponse

interface FirestoreSource {
    suspend fun getPlants(
        query: String,
        limit: Long
    ): List<PlantResponse>

    suspend fun getPlantById(id: String): PlantResponse
    suspend fun recordDetection(detection: DetectionHistoryDocument): String
    suspend fun getDetectionsList(userId: String): List<DetectionHistoryDocument>

}
