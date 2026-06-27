package com.github.andiim.plantscan.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetectionResponse(
    val time: Float,
    val image: ImgzResponse,
    val predictions: List<PredictionResponse>
)

@Serializable
data class PredictionResponse(
    val confidence: Float,
    val x: Float,
    val width: Float,
    val y: Float,
    @SerialName("class") val jsonMemberClass: String,
    val height: Float
)

@Serializable
data class ImgzResponse(
    val width: Float,
    val height: Float
)
