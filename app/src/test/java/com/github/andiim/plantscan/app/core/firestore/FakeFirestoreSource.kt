package com.github.andiim.plantscan.app.core.firestore

import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.ImageContent
import com.github.andiim.plantscan.app.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.app.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.flow.Flow

class FakeFirestoreSource : FirestoreSource {

    private val model = mutableListOf<PlantDocument>()
    fun addPlant(plant: PlantDocument) {
        model.add(plant)
    }

    override suspend fun getPlants(limit: Long, query: String): List<PlantDocument> {
        return model
    }

    override suspend fun getPlantById(id: String): PlantDocument {
        TODO("Not yet implemented")
    }

    override suspend fun getPlantBySpecies(species: String): PlantDocument {
        TODO("Not yet implemented")
    }

    override suspend fun recordDetection(detection: DetectionHistoryDocument): String {
        TODO("Not yet implemented")
    }

    override suspend fun getDetectionsList(userId: String): List<DetectionHistoryDocument> {
        TODO("Not yet implemented")
    }

    override suspend fun sendASuggestion(suggestion: SuggestionDocument): String {
        TODO("Not yet implemented")
    }

    override fun uploadSuggestionImage(content: ImageContent): Flow<String> {
        TODO("Not yet implemented")
    }

}