package com.github.andiim.plantscan.core.network.fake

import JvmUnitTestFakeAssetManager
import com.github.andiim.plantscan.core.network.AppNetworkDataSource
import com.github.andiim.plantscan.core.network.model.DetectionResponse
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeAppNetworkDataSource @Inject constructor(
    private val networkJson: Json,
    private val assets: FakeAssetManager = JvmUnitTestFakeAssetManager,
) : AppNetworkDataSource {
    override suspend fun detect(image: String, confidence: Int): DetectionResponse {
        TODO("Not yet implemented")
    }
}