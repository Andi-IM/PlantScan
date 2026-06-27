package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.network.model.DetectionResponse
import com.github.andiim.plantscan.core.network.model.ImgzResponse
import com.github.andiim.plantscan.core.network.model.PredictionResponse
import org.junit.Test
import kotlin.test.assertEquals

class NetworkEntityKtTest {
    private val imgResponse = ImgzResponse(
        width = 400f,
        height = 400f,
    )

    private val predictionModel = PredictionResponse(
        confidence = 0.4f,
        x = 100f,
        y = 200f,
        jsonMemberClass = "test",
        width = 400f,
        height = 400f,
    )

    private val networkModel = DetectionResponse(
        time = 30f,
        image = imgResponse,
        predictions = listOf(predictionModel),
    )

    @Test
    fun `DetectionResponse can be mapped to ObjectDetection`() {
        val model = networkModel.asExternalModel()
        assertEquals(networkModel.time, model.time)
    }

    @Test
    fun `ImgzResponse can be mapped to Imgz`() {
        val model = imgResponse.asExternalModel()
        assertEquals(imgResponse.height, model.height)
        assertEquals(imgResponse.width, model.width)
    }

    @Test
    fun `PredictionResponse can be mappped to Prediction`() {
        val model = predictionModel.asExternalModel()
        assertEquals(predictionModel.height, model.height)
        assertEquals(predictionModel.width, model.width)
        assertEquals(predictionModel.x, model.x)
        assertEquals(predictionModel.y, model.y)
        assertEquals(predictionModel.jsonMemberClass, model.jsonMemberClass)
        assertEquals(predictionModel.confidence, model.confidence)
    }
}
