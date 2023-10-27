package com.github.andiim.plantscan.core.firestore.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class HistoryDocument(
    @DocumentId val id: String? = null,
    @ServerTimestamp val timestamp: Timestamp? = null,
    val userId: String = "",
    val plantRef: String = "",
    val accuracy: Float = 0f
)
