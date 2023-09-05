package com.github.andiim.plantscan.app.core.domain.usecase.firebase_services

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse

interface FirestoreSource {
    suspend fun getPlants(
        query: String,
        limit: Long
    ): List<PlantResponse>

    suspend fun getPlantById(id: String): PlantResponse

    suspend fun recordDetection(detection: DetectionHistoryDocument): String
    suspend fun getDetectionsList(): List<DetectionHistoryDocument>

}
