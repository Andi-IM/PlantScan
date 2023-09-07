package com.github.andiim.plantscan.app.core.domain.model

import android.graphics.Bitmap
import java.util.Date

data class Suggestion(
    val userId: String,
    val date: Date,
    val description: String,
    val image: Bitmap
)