package com.github.andiim.plantscan.app.core.domain.model

data class DetectResult(val label: String, val prob: Float)

data class ObjectDetection(
    val image: Imgz,
    val predictions: List<Prediction>
)

data class Prediction(
    val confidence: Float,
    val x: Float,
    val width: Int,
    val y: Float,
    val jsonMemberClass: String,
    val height: Int
)

data class Imgz(
    val width: Int,
    val height: Int
)