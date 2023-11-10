package com.github.andiim.plantscan.core.data.testdoubles

import com.github.andiim.plantscan.core.network.PsNetworkDataSource
import com.github.andiim.plantscan.core.network.fake.FakePsNetworkDataSource
import com.github.andiim.plantscan.core.network.model.DetectionResponse
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.serialization.json.Json

class TestPsNetworkDataSource : PsNetworkDataSource {
    private val source = FakePsNetworkDataSource(
        UnconfinedTestDispatcher(),
        Json { ignoreUnknownKeys = true },
    )

    override suspend fun detect(image: String, confidence: Int, overlap: Int): DetectionResponse =
        runBlocking {
            source.detect(image, confidence, overlap)
        }
}
