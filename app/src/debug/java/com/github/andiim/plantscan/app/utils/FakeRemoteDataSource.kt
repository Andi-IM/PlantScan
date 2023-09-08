package com.github.andiim.plantscan.app.utils

import android.graphics.Bitmap
import com.github.andiim.plantscan.app.core.data.Resource
import com.github.andiim.plantscan.app.core.data.source.network.Dispatcher
import com.github.andiim.plantscan.app.core.data.source.network.PsDispatchers.IO
import com.github.andiim.plantscan.app.core.data.source.network.fake.FakeAssetManager
import com.github.andiim.plantscan.app.core.firestore.FirestoreSource
import com.github.andiim.plantscan.app.core.firestore.model.DetectionHistoryDocument
import com.github.andiim.plantscan.app.core.firestore.model.PlantDocument
import com.github.andiim.plantscan.app.core.firestore.model.SuggestionDocument
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class FakeRemoteDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = JvmUnitTestFakeAssetManager,
) : FirestoreSource {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPlants(query: String, limit: Long): List<PlantDocument> =
        withContext(ioDispatcher) {
            assets.open(PLANTS_ASSET).use(networkJson::decodeFromStream)
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPlantById(id: String): PlantDocument =
        withContext(ioDispatcher) {
            assets.open(PLANT_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun recordDetection(detection: DetectionHistoryDocument): String {
        return "Success"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getDetectionsList(userId: String): List<DetectionHistoryDocument> =
        withContext(ioDispatcher) {
            assets.open(DETECT_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun sendASuggestions(suggestion: SuggestionDocument): String {
        TODO("Not yet implemented")
    }

    override suspend fun updateASuggestion(suggestion: SuggestionDocument) {
        TODO("Not yet implemented")
    }

    override fun uploadSuggestionImage(image: Bitmap, ref: String): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val PLANTS_ASSET = "plants.json"
        private const val PLANT_ASSET = "plant.json"
        private const val DETECT_ASSET = "detect.json"
    }

}