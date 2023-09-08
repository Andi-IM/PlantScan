package com.github.andiim.plantscan.app.core.domain.model

import android.graphics.Bitmap
import java.util.Date

data class Suggestion(
    val id: String?,
    val userId: String,
    val date: Date?,
    val description: String,
    val image: Bitmap?,
    val imageUrl: String?,
) {
    constructor(
        userId: String = "",
        description: String = "",
        image: Bitmap? = null,
    ) : this(null, userId, null, description, image, null)
}