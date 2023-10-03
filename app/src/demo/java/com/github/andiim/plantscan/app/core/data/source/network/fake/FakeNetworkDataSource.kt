package com.github.andiim.plantscan.app.core.data.source.network.fake

import com.github.andiim.plantscan.app.core.data.source.network.Dispatcher
import com.github.andiim.plantscan.app.core.data.source.network.NetworkDataSource
import com.github.andiim.plantscan.app.core.data.source.network.PsDispatchers.IO
import com.github.andiim.plantscan.app.core.data.source.network.model.DetectionResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

class FakeNetworkDataSource @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json,
    private val assets: FakeAssetManager,
) : NetworkDataSource {

    companion object {
        private const val DETECT_ASSET = "detect.json"
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun detect(image: String, confidence: Int): DetectionResponse =
        withContext(ioDispatcher) {
            assets.open(DETECT_ASSET).use(networkJson::decodeFromStream)
        }

}