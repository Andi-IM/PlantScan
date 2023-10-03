package com.github.andiim.plantscan.app.core.domain.model

import kotlinx.datetime.Instant

data class DetectionHistory(
    val id: String? = null,
    val timestamp: Instant? = null,
    val plantRef: String,
    val userId: String,
    val accuracy: Float
) {
    constructor(plantRef: String, userId: String, accuracy: Float) : this(
        null,
        null,
        plantRef,
        userId,
        accuracy
    )
}
