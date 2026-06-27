package com.github.andiim.plantscan.core.firestore.fake

import FirestoreJvmUnitTestFakeAssetManager
import com.github.andiim.plantscan.core.firestore.PsFirebaseDataSource
import com.github.andiim.plantscan.core.firestore.fake.model.PlantJson
import com.github.andiim.plantscan.core.firestore.fake.model.toDocument
import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.core.firestore.model.SuggestionDocument
import com.github.andiim.plantscan.core.network.AppDispatchers.IO
import com.github.andiim.plantscan.core.network.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

@OptIn(ExperimentalSerializationApi::class)
class FakePsFirebaseDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = FirestoreJvmUnitTestFakeAssetManager,
) : PsFirebaseDataSource {

    private suspend fun getPlantsJson(): List<PlantJson> = withContext(ioDispatcher) {
        assets.open(PLANTS_ASSET).use(networkJson::decodeFromStream)
    }

    override suspend fun getPlants(): List<PlantDocument> =
        getPlantsJson().map(PlantJson::toDocument)

    private suspend fun getPlantJson(): PlantJson = withContext(ioDispatcher) {
        assets.open(PLANT_ASSET).use(networkJson::decodeFromStream)
    }

    override suspend fun getPlantById(id: String): PlantDocument = getPlantJson().toDocument()

    private val _detections = mutableListOf<HistoryDocument>()

    override suspend fun recordDetection(detection: HistoryDocument): String {
        _detections.add(detection)
        return detection.id.toString()
    }

    override suspend fun getDetectionHistories(userId: String): List<HistoryDocument> =
        _detections

    override suspend fun getDetectionDetail(historyId: String): HistoryDocument =
        _detections.first { it.id == historyId }

    override suspend fun sendSuggestion(suggestionDocument: SuggestionDocument): String {
        return suggestionDocument.id
    }

    companion object {
        // private const val HISTORY_ASSET = "history.json"
        private const val PLANTS_ASSET = "plants.json"
        private const val PLANT_ASSET = "plant.json"
    }
}
