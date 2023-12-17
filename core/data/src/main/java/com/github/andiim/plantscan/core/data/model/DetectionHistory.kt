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
    timestamp = timestamp?.toInstantKtx()!!,
    plantRef = plantRef,
    userId = userId,
    accuracy = accuracy,
    image = image,
    detections = detections.map(LabelPredictDocument::asExternalModel),
    time = time,
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
    timestamp = Date.from(timestamp.toJavaInstant()),
    time = time,
)

fun LabelPredict.asDocument() = LabelPredictDocument(
    objectClass = objectClass,
    confidence = confidence,
)
