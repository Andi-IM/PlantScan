package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.DetectionHistory
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.util.Date

data class DetectionHistoryDocument(
    @DocumentId val id: String? = null,
    @ServerTimestamp val timestamp: Timestamp? = null,
    val userId: String = "",
    val plantRef: String = "",
    val accuracy: Float = 0f
) {
    fun toModel() = DetectionHistory(
        id = this.id,
        timestamp = this.timestamp?.toInstant(),
        plantRef = this.plantRef,
        userId = this.userId,
        accuracy = this.accuracy
    )

    companion object {
        fun fromModel(detection: DetectionHistory): DetectionHistoryDocument =
            DetectionHistoryDocument(
                id = detection.id,
                timestamp = detection.timestamp?.toTimestamp(),
                userId = detection.userId,
                plantRef = detection.plantRef,
                accuracy = detection.accuracy,
            )
    }
}

fun Timestamp.toInstant(): Instant {
    val milliseconds = this.seconds * 1000 + this.nanoseconds / 1000000
    return Instant.fromEpochMilliseconds(milliseconds)
}

fun Instant.toTimestamp(): Timestamp {
    val ms = this.toEpochMilliseconds()
    val sec = ms / 1000
    val ns: Int = ((ms % 1000) * 1000000).toInt()

    return Timestamp(sec, ns)
}

fun Instant.toDate(): Date {
    return Date.from(this.toJavaInstant())
}
