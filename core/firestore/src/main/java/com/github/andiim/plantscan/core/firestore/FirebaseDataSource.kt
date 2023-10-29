package com.github.andiim.plantscan.core.firestore

import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing API calls to the App Firestore backend.
 */
interface FirebaseDataSource {
    suspend fun getPlants(): Flow<List<PlantDocument>>
    suspend fun getPlantById(id: String): Flow<PlantDocument>
    suspend fun recordDetection(detection: HistoryDocument): String
    suspend fun getDetectionHistories(id: String): List<HistoryDocument>
    suspend fun sendSuggestion(suggestionDocument: SuggestionDocument): String
}
