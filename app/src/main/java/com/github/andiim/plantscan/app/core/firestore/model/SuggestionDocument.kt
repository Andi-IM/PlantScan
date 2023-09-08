package com.github.andiim.plantscan.app.core.firestore.model

import android.graphics.Bitmap
import com.github.andiim.plantscan.app.core.domain.model.Suggestion
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SuggestionDocument(
    @DocumentId val id: String? = "",
    val userId: String = "",
    @ServerTimestamp val date: Date? = null,
    val description: String = "",
    @Exclude val image: Bitmap? = null,
    val imageUrl: String? = null,
) {
    companion object {
        fun fromModel(suggestion: Suggestion) = SuggestionDocument(
            suggestion.id,
            suggestion.userId,
            suggestion.date,
            suggestion.description,
            suggestion.image,
            suggestion.imageUrl
        )
    }
}