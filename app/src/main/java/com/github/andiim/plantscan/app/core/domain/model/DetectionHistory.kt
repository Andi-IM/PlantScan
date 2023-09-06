package com.github.andiim.plantscan.app.core.domain.model

import kotlinx.datetime.Instant

data class DetectionHistory(
    val id: String,
    val timestamp: Instant,
    val plantRef: String,
    val userId: String,
    val accuracy: Float
)