package com.github.andiim.plantscan.app.core.data.source.network.model

import com.github.andiim.plantscan.app.core.domain.model.Imgz
import com.github.andiim.plantscan.app.core.domain.model.ObjectDetection
import com.github.andiim.plantscan.app.core.domain.model.Prediction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetectionResponse(
    val image: ImgzResponse,
    val predictions: List<PredictionResponse>
) {
    fun toModel(): ObjectDetection {
        return ObjectDetection(
            image = this.image.toModel(),
            predictions = this.predictions.map { it.toModel() }
        )
    }
}

@Serializable
data class PredictionResponse(
    val confidence: Float,
    val x: Float,
    val width: Float,
    val y: Float,
    @SerialName("class") val jsonMemberClass: String,
    val height: Float
) {
    fun toModel(): Prediction {
        return Prediction(
            confidence = this.confidence,
            x = this.x,
            y = this.y,
            width = this.width,
            height = this.height,
            jsonMemberClass = this.jsonMemberClass
        )
    }
}

@Serializable
data class ImgzResponse(
    val width: Float,
    val height: Float
) {
    fun toModel(): Imgz {
        return Imgz(
            width = this.width,
            height = this.height
        )
    }
}
