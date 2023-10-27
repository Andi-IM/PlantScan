package com.github.andiim.plantscan.core.network.fake

import NetworkJvmUnitTestFakeAssetManager
import com.github.andiim.plantscan.core.network.AppDispatchers.IO
import com.github.andiim.plantscan.core.network.AppNetworkDataSource
import com.github.andiim.plantscan.core.network.Dispatcher
import com.github.andiim.plantscan.core.network.model.DetectionResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class FakeAppNetworkDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager = NetworkJvmUnitTestFakeAssetManager,
) : AppNetworkDataSource {
    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun detect(image: String, confidence: Int): DetectionResponse =
        withContext(ioDispatcher) {
            assets.open(DETECT_ASSET).use(networkJson::decodeFromStream)
        }

    companion object {
        const val DETECT_ASSET = "detect.json"
    }
}
