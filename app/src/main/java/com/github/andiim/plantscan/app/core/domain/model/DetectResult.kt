package com.github.andiim.plantscan.app.core.domain.model

import android.graphics.Bitmap

data class ObjectDetection(
    var image: Imgz,
    val predictions: List<Prediction>,
)

data class Prediction(
    val confidence: Float,
    val x: Float,
    val width: Float,
    val y: Float,
    val jsonMemberClass: String,
    val height: Float
)

data class Imgz(
    val width: Float,
    val height: Float,
    val data: Bitmap? = null
)