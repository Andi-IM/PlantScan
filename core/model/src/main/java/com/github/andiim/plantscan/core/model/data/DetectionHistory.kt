package com.github.andiim.plantscan.core.model.data

import kotlinx.datetime.Instant

data class DetectionHistory(
    val id: String?,
    val timestamp: Instant,
    val plantRef: String,
    val userId: String,
    val accuracy: Float,
    val image: String,
    val detections: List<LabelPredict>,
)

data class LabelPredict(
    val objectClass: String = "",
    val confidence: Float = 0f,
)
