package com.github.andiim.plantscan.app.core.domain.model

import android.graphics.Bitmap
import java.util.Date

data class Suggestion(
    val id: String,
    val userId: String,
    val date: Date?,
    val description: String,
    val imageUrl: List<String>? = null,
    val image: List<Bitmap>,
) {
    constructor(
        userId: String = "",
        description: String = "",
        image: List<Bitmap> = listOf(),
    ) : this("", userId, null, description, null, image)
}