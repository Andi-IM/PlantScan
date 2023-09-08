package com.github.andiim.plantscan.app.core.firestore.model

import android.graphics.Bitmap

data class ImageContent(
    val image: Bitmap,
    val ref: String
)