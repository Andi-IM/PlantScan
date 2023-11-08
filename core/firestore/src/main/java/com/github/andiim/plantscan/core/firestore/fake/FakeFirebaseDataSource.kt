package com.github.andiim.plantscan.core.firestore.fake

import FirestoreJvmUnitTestFakeAssetManager
import com.github.andiim.plantscan.core.firestore.FirebaseDataSource
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

class FakeFirebaseDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = FirestoreJvmUnitTestFakeAssetManager,
) : FirebaseDataSource {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPlants(): List<PlantDocument> =
        withContext(ioDispatcher) {
            assets.open(PLANTS_ASSET).use(networkJson::decodeFromStream)
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPlantById(id: String): PlantDocument =
        withContext(ioDispatcher) {
            assets.open(PLANT_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun recordDetection(detection: HistoryDocument): String = ""

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getDetectionHistories(id: String): List<HistoryDocument> =
        withContext(ioDispatcher) {
            assets.open(HISTORY_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun sendSuggestion(suggestionDocument: SuggestionDocument): String = ""

    companion object {
        private const val HISTORY_ASSET = "history.json"
        private const val PLANTS_ASSET = "plants.json"
        private const val PLANT_ASSET = "plant.json"
    }
}
