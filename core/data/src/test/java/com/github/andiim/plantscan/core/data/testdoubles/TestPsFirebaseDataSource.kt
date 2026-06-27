package com.github.andiim.plantscan.core.data.testdoubles

import com.github.andiim.plantscan.core.firestore.PsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.fake.FakePsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.json.Json

/**
 * Test double for [PsFirebaseDataSource].
 */
class TestPsFirebaseDataSource : PsFirebaseDataSource {
    private val source = FakePsFirebaseDataSource(
        UnconfinedTestDispatcher(),
        Json { ignoreUnknownKeys = true },
    )

    private val allPlants = runBlocking { source.getPlants() }

    override suspend fun getPlants(): List<PlantDocument> = allPlants

    override suspend fun getPlantById(id: String): PlantDocument = runBlocking {
        source.getPlantById(id)
    }

    override suspend fun recordDetection(detection: HistoryDocument): String = runBlocking {
        source.recordDetection(detection)
    }

    override suspend fun getDetectionHistories(userId: String): List<HistoryDocument> =
        runBlocking {
            source.getDetectionHistories(userId)
        }

    override suspend fun getDetectionDetail(historyId: String): HistoryDocument = runBlocking {
        source.getDetectionDetail(historyId)
    }

    override suspend fun sendSuggestion(suggestionDocument: SuggestionDocument): String =
        runBlocking {
            source.sendSuggestion(suggestionDocument)
        }
}
