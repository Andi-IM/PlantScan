package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class DetectionHistoryDocument(
    @DocumentId val id: String = "",
    @ServerTimestamp val timestamp: Instant = Clock.System.now(),
    val plantRef: String = "",
    val accuracy: Float = 0f
) {
    fun toModel() = DetectionHistory(
        id = this.id,
        timestamp = this.timestamp,
        plantRef = this.plantRef,
        accuracy = this.accuracy
    )

    companion object {
        fun fromModel(detection: DetectionHistory): DetectionHistoryDocument =
            DetectionHistoryDocument(
                id = detection.id,
                timestamp = detection.timestamp,
                plantRef = detection.plantRef,
                accuracy = detection.accuracy,
            )
    }
}