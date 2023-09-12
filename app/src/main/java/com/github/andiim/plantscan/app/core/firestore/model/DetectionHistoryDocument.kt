package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.util.Date

data class DetectionHistoryDocument(
    @DocumentId val id: String? = null,
    @ServerTimestamp val timestamp: Date? = null,
    val userId: String = "",
    val plantRef: String = "",
    val accuracy: Float = 0f
) {
    fun toModel() = DetectionHistory(
        id = this.id,
        timestamp = this.timestamp?.toinstant(),
        plantRef = this.plantRef,
        userId = this.userId,
        accuracy = this.accuracy
    )

    companion object {
        fun fromModel(detection: DetectionHistory): DetectionHistoryDocument =
            DetectionHistoryDocument(
                id = detection.id,
                timestamp = detection.timestamp?.toDate(),
                userId = detection.userId,
                plantRef = detection.plantRef,
                accuracy = detection.accuracy,
            )
    }
}

fun Date.toinstant(): Instant {
    return Instant.parse(this.toString())
}

fun Instant.toDate(): Date {
    return Date.from(this.toJavaInstant())
}
