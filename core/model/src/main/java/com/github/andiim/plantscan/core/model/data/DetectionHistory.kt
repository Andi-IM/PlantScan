package com.github.andiim.plantscan.core.model.data

import kotlinx.datetime.Instant

data class DetectionHistory(
    val id: String,
    val timeStamp: Instant,
    val plantRef: String,
    val userId: String,
    val acc: Float,
)
