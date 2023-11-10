@file:UseSerializers(DateSerializer::class)

package com.github.andiim.plantscan.core.firestore.model

import com.github.andiim.plantscan.core.firestore.utils.DateSerializer
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.Date

@Serializable
data class HistoryDocument(
    @DocumentId val id: String? = null,
    @ServerTimestamp val timestamp: Date? = null,
    val userId: String = "",
    val plantRef: String = "",
    val accuracy: Float = 0f,
    val image: String = "",
    val detections: List<LabelPredictDocument> = listOf(),
)

@Serializable
data class LabelPredictDocument(
    val objectClass: String = "",
    val confidence: Float = 0f,
)
