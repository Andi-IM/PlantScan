package com.github.andiim.plantscan.app.core.firestore.model

import android.graphics.Bitmap
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SuggestionDocument(
    val userId: String,
    @ServerTimestamp val date: Date,
    val description: String,
    val image: Bitmap
)