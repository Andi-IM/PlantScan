package com.github.andiim.plantscan.app.core.data.source.firebase.firestore.document

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class DetectionDocument(
    @DocumentId val id: String = "",
    @ServerTimestamp val timestamp: Instant = Clock.System.now(),
    val plantRef: String = "",
    val accuracy: Float = 0f
)