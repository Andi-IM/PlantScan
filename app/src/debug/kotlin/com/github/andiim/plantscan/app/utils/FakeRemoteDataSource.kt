package com.github.andiim.plantscan.app.utils

import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.DetectionDocument
import com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document.PlantResponse
import com.github.andiim.plantscan.app.core.data.source.network.Dispatcher
import com.github.andiim.plantscan.app.core.data.source.network.PsDispatchers.IO
import com.github.andiim.plantscan.app.core.data.source.network.fake.FakeAssetManager
import com.github.andiim.plantscan.app.core.domain.usecase.firebase_services.FirestoreSource
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class FakeRemoteDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = JvmUnitTestFakeAssetManager,
) : FirestoreSource {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPlants(query: String, limit: Long): List<PlantResponse> =
        withContext(ioDispatcher) {
            assets.open(PLANTS_ASSET).use(networkJson::decodeFromStream)
        }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getPlantById(id: String): PlantResponse =
        withContext(ioDispatcher) {
            assets.open(PLANT_ASSET).use(networkJson::decodeFromStream)
        }

    override suspend fun recordDetection(detection: DetectionDocument): String {
        return "Success"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun getDetectionsList(): List<DetectionDocument> =
        withContext(ioDispatcher) {
            assets.open(DETECT_ASSET).use(networkJson::decodeFromStream)
        }

    companion object {
        private const val PLANTS_ASSET = "plants.json"
        private const val PLANT_ASSET = "plant.json"
        private const val DETECT_ASSET = "detect.json"
    }

}