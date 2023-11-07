package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import kotlinx.datetime.Instant
import java.util.Date

/**
 * Converts the document as Model.
 *
 * note: ID dan Timestamp must be exist!
 */
fun HistoryDocument.asExternalModel() = DetectionHistory(
    id = id,
    plantRef = plantRef,
    userId = userId,
    acc = accuracy,
    timeStamp = timestamp?.toInstantKtx()!!,
)

fun Date.toInstantKtx(): Instant {
    val millisecond: Long = this.toInstant().toEpochMilli()
    return Instant.fromEpochMilliseconds(epochMilliseconds = millisecond)
}

/**
 * Converts history as document.
 *
 * ID and Timestamp may be null.
 */
fun DetectionHistory.asDocument() = HistoryDocument(
    userId = userId,
    plantRef = plantRef,
    accuracy = acc,
)
