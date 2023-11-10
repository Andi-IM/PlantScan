package com.github.andiim.plantscan.core.network.fake

import NetworkJvmUnitTestFakeAssetManager
import com.github.andiim.plantscan.core.network.model.DetectionResponse
import com.github.andiim.plantscan.core.network.model.ImgzResponse
import com.github.andiim.plantscan.core.network.model.PredictionResponse
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class FakePsNetworkDataSourceTest {
    private lateinit var subject: FakePsNetworkDataSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        subject = FakePsNetworkDataSource(
            ioDispatcher = testDispatcher,
            networkJson = Json { ignoreUnknownKeys = true },
            assets = NetworkJvmUnitTestFakeAssetManager,
        )
    }

    @Test
    fun testDeserializationOfDetection() = runTest(testDispatcher) {
        assertEquals(
            expected = DetectionResponse(
                time = 1.000f,
                image = ImgzResponse(
                    width = 2048f,
                    height = 1371f,
                ),
                predictions = listOf(
                    PredictionResponse(
                        x = 189.5f,
                        y = 100f,
                        width = 163f,
                        height = 186f,
                        jsonMemberClass = "helmet",
                        confidence = 0.544f
                    )
                ),
            ),
            actual = subject.detect("", 1, 1),
        )
    }
}
