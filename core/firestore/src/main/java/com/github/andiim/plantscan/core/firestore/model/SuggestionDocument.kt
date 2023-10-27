package com.github.andiim.plantscan.core.firestore.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SuggestionDocument(
    @DocumentId val id: String = "",
    val userId: String = "",
    @ServerTimestamp val date: Date? = null,
    val description: String = "",
    val images: List<ImageDocument> = listOf(),
)
