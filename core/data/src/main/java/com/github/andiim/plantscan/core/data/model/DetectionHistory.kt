package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.firestore.model.HistoryDocument
import com.github.andiim.plantscan.core.firestore.model.LabelPredictDocument
import com.github.andiim.plantscan.core.model.data.DetectionHistory
import com.github.andiim.plantscan.core.model.data.LabelPredict
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
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
    accuracy = accuracy,
    timestamp = timestamp?.toInstantKtx()!!,
    image = image,
    detections = detections.map(LabelPredictDocument::asExternalModel),
)

fun LabelPredictDocument.asExternalModel() = LabelPredict(
    objectClass = objectClass,
    confidence = confidence,
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
    id = id,
    userId = userId,
    plantRef = plantRef,
    accuracy = accuracy,
    image = image,
    detections = detections.map(LabelPredict::asDocument),
    timestamp = Date.from(timestamp.toJavaInstant())
)

fun LabelPredict.asDocument() = LabelPredictDocument(
    objectClass = objectClass,
    confidence = confidence,
)
