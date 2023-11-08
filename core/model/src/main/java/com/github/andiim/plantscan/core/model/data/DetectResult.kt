package com.github.andiim.plantscan.core.model.data

data class ObjectDetection(
    val time: Float,
    var image: Imgz,
    val predictions: List<Prediction>,
)

data class Prediction(
    val confidence: Float,
    val x: Float,
    val width: Float,
    val y: Float,
    val jsonMemberClass: String,
    val height: Float,
)

data class Imgz(
    val width: Float,
    val height: Float,
    val base64: String? = null,
)
