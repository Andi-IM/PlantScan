package com.github.andiim.plantscan.app.core.firestore

import android.graphics.Bitmap
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.app.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.flow.Flow

interface FirestoreSource {
    suspend fun getPlants(
        query: String,
        limit: Long
    ): List<PlantDocument>

    suspend fun getPlantById(id: String): PlantDocument
    suspend fun recordDetection(detection: DetectionHistoryDocument): String
    suspend fun getDetectionsList(userId: String): List<DetectionHistoryDocument>
    suspend fun sendASuggestions(suggestion: SuggestionDocument): String
    suspend fun updateASuggestion(suggestion: SuggestionDocument)
    fun uploadSuggestionImage(image: Bitmap, ref: String): Flow<Resource<String>>
}
