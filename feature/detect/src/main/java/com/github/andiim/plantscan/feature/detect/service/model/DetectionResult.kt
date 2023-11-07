package com.github.andiim.plantscan.feature.detect.service.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.Instant

@Parcelize
data class DetectionResult(
    val id: String? = null,
    val timestamp: Instant? = null,
    val imgB64: String,
    val userId: String,
    val accuracy: Float,
) : Parcelable
