package com.github.andiim.plantscan.core.data.model

import com.github.andiim.plantscan.core.firestore.model.SuggestionDocument
import com.github.andiim.plantscan.core.model.data.Suggestion

fun Suggestion.asDocument() = SuggestionDocument(
    id = id,
    userId = userId,
    date = date,
    description = description,
    images = images,
)
