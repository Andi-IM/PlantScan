package com.github.andiim.plantscan.app.core.firestore.model

import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SuggestionDocument(
    @DocumentId val id: String = "",
    val userId: String = "",
    @ServerTimestamp val date: Date? = null,
    val description: String = "",
    val imageUrl: List<String> = listOf(),
) {
    companion object {
        fun fromModel(suggestion: Suggestion) = SuggestionDocument(
            id = suggestion.id,
            userId = suggestion.userId,
            date = suggestion.date,
            description = suggestion.description,
            imageUrl = suggestion.imageUrl
        )
    }
}
