package com.github.andiim.plantscan.core.firestore

import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.firestore.model.SuggestionDocument

/**
 * Interface representing API calls to the App Firestore backend
 */
interface FirebaseDataSource {
    suspend fun getPlants(
        limit: Long,
        query: String = ""
    ): List<PlantDocument>
    suspend fun getPlantById(id: String): PlantDocument
    suspend fun recordDetection(detection: HistoryDocument): String
    suspend fun getDetectionsList(id: String): List<HistoryDocument>
    suspend fun sendSuggestion(suggestionDocument: SuggestionDocument): String
}