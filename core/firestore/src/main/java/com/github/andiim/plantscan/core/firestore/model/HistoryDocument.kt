package com.github.andiim.plantscan.core.firestore.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class HistoryDocument(
    @DocumentId val id: String? = null,
    @ServerTimestamp val timestamp: Date? = null,
    val userId: String = "",
    val plantRef: String = "",
    val accuracy: Float = 0f,
)
